#!/bin/bash

# 获取宿主机内存总量（单位：M）
total_mem=$(grep MemTotal /proc/meminfo | awk '{print int($2/1024)}')
echo "Total memory in MB: $total_mem"

# 将内存总量转为单位：G 向上取整
mem_in_g=$(( (total_mem + 1023) / 1024 ))
echo "Total memory in GB (rounded up): $mem_in_g"

# 判断内存大小，按需设定内存分配比例
if [ $mem_in_g -lt 16 ]; then
  mem_ratio=50
elif [ $mem_in_g -ge 16 ] && [ $mem_in_g -lt 32 ]; then
  mem_ratio=75
elif [ $mem_in_g -ge 32 ]; then
  mem_ratio=40
fi
echo "Memory allocation ratio: $mem_ratio%"

# 计算堆大小
heap_size=$(($total_mem * $mem_ratio / 100 / 1024))
echo "Heap size in GB: $heap_size"

# 计算Metaspace大小
metaspace_size=$(echo "$heap_size * 1024 * 0.031" | bc | awk '{print int($1+0.5)}' | sed -e 's/^0\{1,\}//')
echo "Metaspace size in GB: $metaspace_size"

# 计算最大Metaspace大小
max_metaspace_size=$(echo "$heap_size * 1024 * 0.078" | bc | awk '{print int($1+0.5)}' | sed -e 's/^0\{1,\}//')
echo "Max Metaspace size in GB: $max_metaspace_size"