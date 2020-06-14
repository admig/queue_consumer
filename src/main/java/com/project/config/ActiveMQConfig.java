package com.project.config;


import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.RedeliveryPolicy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import static org.apache.activemq.ActiveMQSession.INDIVIDUAL_ACKNOWLEDGE;

@Configuration
public class ActiveMQConfig {

    @Value("${spring.activemq.broker-url}")
    public String url;
    @Value("${spring.activemq.user}")
    public String user;
    @Value("${spring.activemq.password}")
    public String password;

    /**
     * 自定义ConnectFactory
     */
    @Bean
    public ActiveMQConnectionFactory connectionFactory(){
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory();
        activeMQConnectionFactory.setPassword(password);
        activeMQConnectionFactory.setUserName(password);
        activeMQConnectionFactory.setBrokerURL(url);
        return activeMQConnectionFactory;
    }

    /**
     * 消息重发
     * @return
     */
    @Bean
    public RedeliveryPolicy redeliveryPolicy(){
        RedeliveryPolicy redeliveryPolicy = new RedeliveryPolicy();

        //每次尝试重新发送失败，增长等待时间
        redeliveryPolicy.setUseExponentialBackOff(true);

        //重发次数
        redeliveryPolicy.setMaximumRedeliveries(5);

        //重发时间间隔，默认1000ms
        redeliveryPolicy.setInitialRedeliveryDelay(1000);

        //重发时长递增
        redeliveryPolicy.setBackOffMultiplier(2);

        //是否避免消息碰撞
        redeliveryPolicy.setUseCollisionAvoidance(false);

        //设置重发最大拖延时间
        redeliveryPolicy.setMaximumRedeliveryDelay(-1);

        return redeliveryPolicy;
    }

    //自定义queue的监听
    @Bean
    public JmsListenerContainerFactory jmsListenerContainerQueue(ActiveMQConnectionFactory connectionFactory,RedeliveryPolicy redeliveryPolicy){
        DefaultJmsListenerContainerFactory bean = new DefaultJmsListenerContainerFactory();
        bean.setPubSubDomain(false); //设置为queue
        bean.setSessionTransacted(false); //关闭事务，才能手动签收
        bean.setSessionAcknowledgeMode(INDIVIDUAL_ACKNOWLEDGE);  //手动签收单条消息
        connectionFactory.setRedeliveryPolicy(redeliveryPolicy);  //设置消息重发。
        bean.setConnectionFactory(connectionFactory);
        return bean;
    }


}
