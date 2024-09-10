#!/bin/bash

if [ $# -ne 2 ]; then
    echo "Usage: $0 <Java_PID> <Number of Threads>"
    exit 1
fi

java_pid=$1
n=$2
user=$(ps -o user= -p "$java_pid" | awk '{print $1}')

# 1. 获取进程的jstack线程堆栈
if ! jstack_output=$(sudo -u "$user" jstack -l "$java_pid" 2>/dev/null); then
    echo "Failed to get jstack output. Make sure you have the necessary permissions."
    exit 2
fi

# 2. 找出进程下CPU使用率最高的前N个线程的ID（16进制表示）
top_threads_info=$(top -b -n 1 -H -p "$java_pid" | awk 'NR>7 && $9 != "0.0" {print $1, $9}' | sort -k2 -r | head -n "$n")

# 3. 在jstack输出中匹配"nid={16进制线程id}"，并输出匹配的堆栈
echo "Top $n CPU-consuming threads for Java PID $java_pid (in hexadecimal):"
while IFS= read -r line; do
    thread_id=$(echo "$line" | awk '{print $1}')
    cpu_usage=$(echo "$line" | awk '{print $2}')
    hex_thread_id=$(printf "0x%x" "$thread_id")
    echo "Thread ID: $thread_id($hex_thread_id), CPU Usage: $cpu_usage%"
    echo ""
    echo "$jstack_output" | awk -v hex_id="$hex_thread_id" '
        BEGIN {flag=0}
        /nid='"$hex_thread_id"'/ {flag=1}
        flag && /^$/{flag=0}
        flag
    '
    echo "---------------------------------------------"
done <<< "$top_threads_info"