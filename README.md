#### 多端消息推送框架
支持可靠(至少推送一次)不可靠的, 有序不有序的多端消息推送框架

##### usage
1. 实现MessageRepository
2. 实现MessageReceiptRepository
3. 实现TunnelRepository
4. 实现PushLocker, 必要时需实现为分布式锁
5. 实现Tunnel并注册到TunnelRepository
5. 通过add,onInit,onConnect方法触发推送线程, 队列空或消息通道未连接自动关闭

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
    private TunnelRepository tunnelRepository;

    @Bean
    public MessagePusher messagePusher() {
        return new StandardMessagePusherBuilder()
                .withExecutorService(Executors.newCachedThreadPool())
                .withMessagePushLocker(pushLocker)
                .withMessageReceiptRepository(messageReceiptRepository)
                .withMessageRepository(messageRepository)
                .withTunnelRepository(tunnelRepository)
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