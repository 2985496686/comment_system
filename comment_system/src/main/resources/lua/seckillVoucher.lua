---
--- Generated by EmmyLua(https://github.com/EmmyLua)
--- Created by 123.
--- DateTime: 2022/10/16 16:26
---

---优惠卷id
local voucherId = ARGV[2]
---用户id
local userId = ARGV[1]
---订单id
local orderId = ARGV[3]
--- 库存是否足够
if (tonumber (redis.call('get', 'voucher:stock:'..voucherId)) <= 0 )then
    ---库存不够
    return 1
end
if ((redis.call('sismember','voucher:order:'..voucherId,userId)) == 1) then
    ---重复秒杀
    return 2
end
--- 满足秒杀条件
redis.call('sadd','voucher:order:'..voucherId,userId)
---减库存
redis.call('incrby','voucher:stock:'..voucherId,-1)
--- 发送消息到消息队列
redis.call('xadd','stream.orders','*','id',orderId,'voucherId',voucherId,'userId',userId)
return 0