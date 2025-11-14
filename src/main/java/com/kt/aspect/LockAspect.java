package com.kt.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import com.kt.common.ErrorCode;
import com.kt.common.Lock;
import com.kt.common.Preconditions;

import lombok.RequiredArgsConstructor;

@Aspect
@Component
@RequiredArgsConstructor
public class LockAspect {
	private final AopTransactionManager aopTransactionManager;
	private final RedissonClient redissonClient;

	@Around("@annotation(com.kt.common.Lock) &&@annotation(lock)")
	public Object lock(ProceedingJoinPoint joinPoint, Lock lock) throws Throwable {
		var argument = joinPoint.getArgs();
		var identity = (Long)argument[lock.index()];
		var key = String.format("%s:%d", lock
			.key()
			.name()
			.toLowerCase(), identity);

		var rLock = redissonClient.getLock(key);

		try {
			var available = rLock.tryLock(lock.waitTime(), lock.leaseTime(), lock.timeUnit());

			Preconditions.validate(available, ErrorCode.FAIL_ACQUIRED_LOCK);

			return aopTransactionManager.proceed(joinPoint);
		} finally {
			if (rLock.isHeldByCurrentThread()) {
				rLock.unlock();
			}
		}
	}
}
