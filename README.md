#### 多端消息推送框架
支持可靠(至少送达一次)不可靠的, 有序不有序的多端消息推送框架

##### usage
1. 实现MessageRepository
2. 实现MessageReceiptRepository
3. 实现TunnelFactory
4. 实现PushLocker
5. 实现Tunnel
5. 通过调用add,onInit,onConnect方法触发推送线程, 队列空或消息通道未连接自动关闭

###### config
```$xslt
@Configuration
public class MessagePushConfig {

    @Autowired
    private PushLocker pushLocker;
    @Autowired
    private MessageReceiptRepository messageReceiptRepository;
    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private TunnelFactory tunnelFactory;

    @Bean
    public MessagePusher messagePusher() {
        return new StandardMessagePusherBuilder()
                .withExecutorService(Executors.newCachedThreadPool())
                .withMessagePushLocker(pushLocker)
                .withMessageReceiptRepository(messageReceiptRepository)
                .withMessageRepository(messageRepository)
                .withTunnelRepository(tunnelFactory)
                .withReceiptTimeout(10, TimeUnit.SECONDS).build();
    }
}
```
###### service
```$xslt
@Service
public class Service {

    @Autowired
    private MessagePusher messagePusher;
    @Autowired
    private MyTunnel myTunnel;
    
    @PostConstruct
    private void init() {
        messagePusher.onInit();
    }

    public void add() {
        messagePusher.addToTunnelQueue(new Message(), myTunnel, false);
    }

    public void reportReceipt() {
        messagePusher.reportReceipt("user1", myTunnel, "msg1");
    }
    
    public void onConnect(String receiver, Tunnel tunnel) {
        messagePusher.onConnect(receiver, tunnel);
    }
}
```