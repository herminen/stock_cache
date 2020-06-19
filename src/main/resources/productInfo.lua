local uri_args = ngx.req.get_uri_args()
local productId = tostring(uri_args["productId"])
local shopId = tostring(uri_args["shopId"])

local cache_ngx = ngx.shared.my_cache

local cjson = require("cjson")
--send message for log
local producer = require("resty.kafka.producer")
local broker_list = {
     {host="192.168.79.130", port=9092},
     {host="192.168.79.131", port=9092},
     {host="192.168.79.132", port=9092}
}

local log_json = {}
log_json["uri"] = ngx.var.uri
log_json["args"] = ngx.var.args
log_json["host"] = ngx.var.host
log_json["request_body"] = ngx.var.request_body
log_json["remote_addr"] = ngx.var.remote_addr
log_json["remote_user"] = ngx.var.remote_user
log_json["time_local"] = ngx.var.time_local
log_json["status"] = ngx.var.status
log_json["body_byte_sent"] = ngx.var.body_byte_sent
log_json["http_referer"] = ngx.var.http_referer
log_json["http_user_agent"] = ngx.var.http_user_agent
log_json["http_x_forward_for"] = ngx.var.http_x_forward_for
log_json["upstream_response_time"] = ngx.var.upstream_response_time
log_json["request_time"] = ngx.var.request_time

local message = cjson.encode(log_json)
--define kafka async producer
local async_sender = producer:new(broker_list, {producer_type = "async",batch_num = 1})
-- 发送日志消息,send第二个参数key,用于kafka路由控制:  
-- key为nill(空)时，一段时间向同一partition写入数据  
-- 指定key，按照key的hash写入到对应的partition
local ok, err = async_sender:send("log_prodinfo", productId, message)

if not ok then
    ngx.log(ngx.ERR, "kafka send error:", err)
end


local productCacheKey = "product_key_"..productId
local shopCacheKey = "shop_key_"..shopId

local productCache = cache_ngx:get(productCacheKey)
local shopCache = cache_ngx:get(shopCacheKey)

if productCache == "" or productCache == nil then
      local http = require("resty.http")
      local httpc = http.new()

      local resp, err = httpc:request_uri("http://192.168.18.17:8080", {
            method = "GET",
            path = "/product/getProductInfo?prodId="..productId

      })
    

      productCache = resp.body
      cache_ngx:set(productCacheKey, productCache, 5 * 60)
end

if shopCache == "" or shopCache == nil then
     local http = require("resty.http")
     local httpc = http.new()

     local resp, err = httpc:request_uri("http://192.168.18.17:8080", {
           method = "GET",
           path = "/product/getShopInfo?shopId="..shopId

     })

     shopCache = resp.body
     cache_ngx:set(shopCacheKey, shopCache, 5 * 60)
end

--local cjson = require("cjson")
local productCacheJSON = cjson.decode(productCache)
local shopCacheJSON = cjson.decode(shopCache)

local context = {
    productId = productCacheJSON.id,
    productName = productCacheJSON.name,
    productPrice = productCacheJSON.price,
    productPictureList = productCacheJSON.pictureList,
    productSpecification = productCacheJSON.specification,
    productService = productCacheJSON.service,
    productColor = productCacheJSON.color,
    productSize = productCacheJSON.size,
    shopId = shopCacheJSON.id,
    shopLevel = shopCacheJSON.level,
    shopGoodCommentRate = shopCacheJSON.goodCommentRate
}

local template = require("resty.template")
template.render("product.html", context)
