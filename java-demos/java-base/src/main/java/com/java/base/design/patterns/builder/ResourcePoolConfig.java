package com.java.base.design.patterns.builder;

import java.util.Objects;

/**
 *@author : wh
 *@date : 2023/11/6 19:41
 *@description: 建造者模式
 */
public class ResourcePoolConfig {

	private String name;
	private int maxTotal;
	private int maxIdle;
	private int minIdle;

	private ResourcePoolConfig(Builder builder) {
		this.name = builder.name;
		this.maxIdle = builder.maxIdle;
		this.minIdle = builder.minIdle;
	}

	public static class Builder {
		private static final int DEFAULT_MAX_TOTAL = 8;
		private static final int DEFAULT_MAX_IDLE = 8;
		private static final int DEFAULT_MIN_IDELE = 0;

		private String name;
		private int maxTotal = DEFAULT_MAX_TOTAL;
		private int maxIdle = DEFAULT_MAX_IDLE;
		private int minIdle = DEFAULT_MIN_IDELE;

		public ResourcePoolConfig build() {
			if (Objects.isNull(name)) {
				throw new IllegalArgumentException("...");
			}
			if (maxIdle > maxTotal) {
				throw new IllegalArgumentException("...");
			}
			if (minIdle > maxTotal || minIdle > maxIdle) {
				throw new IllegalArgumentException("...");
			}
			return new ResourcePoolConfig(this);
		}

		public Builder setName(String name) {
			if (Objects.isNull(name)) {
				throw new IllegalArgumentException("...");
			}
			this.name = name;
			return this;
		}

		public Builder setMaxTotal(int maxTotal) {
			if (maxTotal <= 0) {
				throw new IllegalArgumentException("...");
			}
			this.maxTotal = maxTotal;
			return this;
		}

		public Builder setMaxIdle(int maxIdle) {
			if (maxIdle < 0) {
				throw new IllegalArgumentException("...");
			}
			this.maxIdle = maxIdle;
			return this;
		}

		public Builder setMinIdle(int minIdle) {
			if (minIdle < 0) {
				throw new IllegalArgumentException("...");
			}
			this.minIdle = minIdle;
			return this;
		}
	}

	public static void main(String[] args) {
		ResourcePoolConfig config = new ResourcePoolConfig.Builder()
				.setName("小奏")
				.setMaxTotal(16)
				.setMaxIdle(10)
				.setMinIdle(12)
				.build();
	}

}
