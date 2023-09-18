package com.java.base.jmx;

import com.alibaba.fastjson.JSON;
import java.util.Set;
import javax.management.MBeanInfo;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

/**
 * @author : wh
 * @date : 2023/9/16 14:29
 * @description:
 */
public class JMXClient {
        public static void main(String[] args) throws Exception {
            String jmxHost = "127.0.0.1";
            int jmxPort = 9988;
            String jmxUrl = String.format("service:jmx:rmi:///jndi/rmi://%s:%d/jmxrmi", jmxHost, jmxPort);
            
            monitor(jmxUrl);
        }

        public static void monitor(String url) throws Exception{
            JMXServiceURL jmxServiceURL = new JMXServiceURL
                    (url);
            JMXConnector jmxc = JMXConnectorFactory.connect(jmxServiceURL, null);
            MBeanServerConnection mBeanServer = jmxc.getMBeanServerConnection();

            ObjectName objectNamePattern = new ObjectName("kafka.server:type=BrokerTopicMetrics,*");

            Set<ObjectName> objectNames = mBeanServer.queryNames(objectNamePattern, null);
            for (ObjectName objectName : objectNames) {
                MBeanInfo mBeanInfo = mBeanServer.getMBeanInfo(objectName);
                JSON.toJSONString(mBeanInfo);
            }

           /* String[] domains = mBeanServer.getDomains();
            for (String domain : domains) {
                MBeanInfo info = mBeanServer.getMBeanInfo(new ObjectName(domain));
                JSON.toJSONString(info);
//                System.out.println(domain);
            }*/
        }
    }
    