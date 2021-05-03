---------redisson
Semaphore(0)信号量的tryAcquire方法来阻塞,redis的发布、订阅来唤醒

//如releaseTime不传，为-1，就会一个线程30秒的每1/3时间做一次续命，防止挂了一直锁 
lock.tryLock(waitTime, releaseTime, TimeUnit.SECONDS); 

源码的RedissonLock的tryLock方法中有 subscribeFuture.getNow().getLatch().tryAcquire 就是用Semaphore(0)阻塞

源码的RedissonLock的tryAcquireAsync方法中有 scheduleExpirationRenewal(threadId)->renewExpiration(); 就是releaseTime为-1续命
源码的RedissonLock的 subscribe(threadId);是发布、订阅，一个自定义channel名字，在unlock时有push命令

---------zookeeper
是通过临时有序节点来阻塞，排队在最后，监听节点变动，如是第一个就拿到锁





