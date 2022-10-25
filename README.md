# LogCatcher

### Logcatcher

```mermaid
graph LR;
	应用创建 --> 创建全局Context --> 监听开机广播 --> 判断是否启用日志存储 --> 启动服务 --> 启动日志获取和存储;
	创建全局Context --> 监听日期变化广播 --> 判断是否启用日志存储;
```
