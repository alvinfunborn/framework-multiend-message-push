#### 多端消息推送框架
可靠的, 至少推送一次的, 支持有序的多端消息推送框架

##### usage
1. 实现MessageRepository
2. 实现MessageReceiptRepository
3. 实现PushManager
4. 实现MessagePushLocker, 必要时需实现未分布式锁
5. 手动触发推送线程, 队列空或消息通道未连接自动关闭

###### config
```$xslt
@Configuration
public class MessagePushConfig {
    
    @Autowired
    private MessagePushLocker messagePushLocker;
    @Autowired
    private MessageReceiptRepository messageReceiptRepository;
    @Autowired
    private MessageRepository<String> messageRepository;
    @Autowired
    private PushManager<String> pushManager;

    @Bean
    public MessagePusher messagePusher() {
        return new StandardMessagePusherBuilder<String>()
                .withExecutorService(Executors.newCachedThreadPool())
                .withMessagePushLocker(messagePushLocker)
                .withMessageReceiptRepository(messageReceiptRepository)
                .withMessageRepository(messageRepository)
                .withPushManager(pushManager)
                .withReceiptTimeout(10, TimeUnit.SECONDS).build();
    }
}
```
###### service
```$xslt
@Service
public class Service {

    @Autowired
    private MessagePusher<Model> messagePusher;
    
    @PostConstruct
    private void init() {
        messagePusher.onInit();
    }

    public void triggerPush() {
        messagePusher.add("1", new Model("1", 1), true, true);
    }

    public void reportReceipt() {
        messagePusher.reportReceipt("1", "sdf");
    }
}
```