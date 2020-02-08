package com.djl.dmall.rabbit.service;

import com.djl.dmall.rabbit.bean.Order;
import com.djl.dmall.rabbit.bean.TestUser;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.io.IOException;


@Service
public class UserService {

    /*@RabbitListener(queues = {"world"})
    public void receiveUserMessage(Message message, TestUser testUser, Channel channel) throws IOException {
        System.out.println("收到的信息是：" + message.getClass());
        System.out.println("收到的信息是：" + testUser.getUsername());

        //拒绝：可以把消息拒绝，让rabbitmq在发送给其他人
        channel.basicReject(message.getMessageProperties().getDeliveryTag(), false);
    }*/
    @RabbitListener(queues = {"order-queue"})
    public void receiveOrder(Order order, Message message, Channel channel) throws IOException {

        System.out.println("监听到订单生成：" + order);
        Long skuID = order.getSkuId();
        Integer num = order.getNum();
        System.out.println("库存为【" + skuID + "】的商品正在减少【" + num + "】件");

        if (num % 7 == 0) {
            System.out.println("商品为【" + skuID + "】的库存扣除失败");
            //回复消息处理失败
            channel.basicNack(message.getMessageProperties().getDeliveryTag(),
                    false, true);

            throw new RuntimeException("error......error......");
        }

        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    }

    @RabbitListener(queues = {"user.order.queue"})
    public void closeOrder(Order order, Message message, Channel channel) throws IOException {

        try {
            System.out.println("正在关闭订单：" + order.getOrderSn());
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            channel.basicNack(message.getMessageProperties().getDeliveryTag(),
                false, true);
            throw new RuntimeException("error......");
        }

    }
}
