## 背景

一般`spring-boot`项目都不用管打包的，用`spring-boot-maven-plugin`插件打成一个jar就可以了。

但是一些中间件比如`RocketMQ`这种多个模块，又需要打成一个jar包，这时候就需要用到`maven-assembly-plugin`插件。


## 使用

1. 引入插件

```xml
            <build>
                <plugins>
                    <plugin>
                        <artifactId>maven-assembly-plugin</artifactId>
                        <executions>
                            <execution>
                                <id>release-all</id>
                                <goals>
                                    <goal>single</goal>
                                </goals>
                                <phase>package</phase>
                                <configuration>
                                    <descriptors>
                                        <descriptor>release.xml</descriptor>
                                    </descriptors>
                                    <appendAssemblyId>false</appendAssemblyId>
                                </configuration>
                            </execution>
                        </executions>
                    </plugin>
                </plugins>
                <finalName>rocketmq-${project.version}</finalName>
            </build>
```

核心是配置文件`release.xml`，这个文件是自定义的，可以根据自己的需求来配置。

2. 配置文件`release.xml`

```xml
<?xml version="1.0" encoding="UTF-8"?>
<assembly>
    <id>all</id>
    <includeBaseDirectory>true</includeBaseDirectory>
    <formats>
        <format>dir</format>
        <format>tar.gz</format>
        <format>zip</format>
    </formats>
    <fileSets>
        <fileSet>
            <directory>../</directory>
            <includes>
                <include>README.md</include>
            </includes>
        </fileSet>

        <fileSet>
            <includes>
                <include>conf/**</include>
                <include>benchmark/*</include>
                <include>agent/*</include>
            </includes>
        </fileSet>

        <fileSet>
            <includes>
                <include>bin/**</include>
            </includes>
            <fileMode>0755</fileMode>
        </fileSet>
    </fileSets>

    <files>
        <file>
            <source>LICENSE-BIN</source>
            <destName>LICENSE</destName>
        </file>
        <file>
            <source>NOTICE-BIN</source>
            <destName>NOTICE</destName>
        </file>
        <file>
            <source>../broker/src/main/resources/rmq.broker.logback.xml</source>
            <destName>conf/rmq.broker.logback.xml</destName>
        </file>
        <file>
            <source>../client/src/main/resources/rmq.client.logback.xml</source>
            <destName>conf/rmq.client.logback.xml</destName>
        </file>
        <file>
            <source>../controller/src/main/resources/rmq.controller.logback.xml</source>
            <destName>conf/rmq.controller.logback.xml</destName>
        </file>
        <file>
            <source>../namesrv/src/main/resources/rmq.namesrv.logback.xml</source>
            <destName>conf/rmq.namesrv.logback.xml</destName>
        </file>
        <file>
            <source>../tools/src/main/resources/rmq.tools.logback.xml</source>
            <destName>conf/rmq.tools.logback.xml</destName>
        </file>
        <file>
            <source>../proxy/src/main/resources/rmq.proxy.logback.xml</source>
            <destName>conf/rmq.proxy.logback.xml</destName>
        </file>
    </files>

    <moduleSets>
        <moduleSet>
            <useAllReactorProjects>true</useAllReactorProjects>
            <includes>
                <include>org.apache.rocketmq:rocketmq-container</include>
                <include>org.apache.rocketmq:rocketmq-broker</include>
                <include>org.apache.rocketmq:rocketmq-tools</include>
                <include>org.apache.rocketmq:rocketmq-client</include>
                <include>org.apache.rocketmq:rocketmq-namesrv</include>
                <include>org.apache.rocketmq:rocketmq-example</include>
                <include>org.apache.rocketmq:rocketmq-openmessaging</include>
                <include>org.apache.rocketmq:rocketmq-controller</include>
            </includes>
            <binaries>
                <outputDirectory>lib/</outputDirectory>
                <unpack>false</unpack>
                <dependencySets>
                    <dependencySet>
                        <outputDirectory>lib/</outputDirectory>
                        <excludes>
                            <exclude>io.jaegertracing:jaeger-core</exclude>
                            <exclude>io.jaegertracing:jaeger-client</exclude>
                        </excludes>
                    </dependencySet>
                </dependencySets>
            </binaries>
        </moduleSet>
    </moduleSets>
</assembly>

```

- includeBaseDirectory 当设置为true时，表示在分发包中包含基础目录。基础目录通常是项目的根目录。


- formats 指定生成的分发包的格式。这里指定了三种格式：dir（目录结构）、tar.gz（压缩包）、zip（压缩包）。这意味着会生成三种格式的分发包

- 第一个fileSet包括了项目根目录上层的README.md文件。
- 第二个fileSet包括了conf目录下的所有内容、benchmark目录下的所有文件、以及agent目录下的所有文件。
- 第三个fileSet包括了bin目录下的所有内容，并且为这些文件设置了文件模式0755（表示文件是可执行的）。

- <useAllReactorProjects>设置为true表示包含当前Maven反应堆中的所有项目。
- <includes>标签指定了要包含的模块。
- <binaries>标签定义了二进制文件的设置，包括输出目录和是否解包依赖项。
- <dependencySets>定义了依赖项的设置，包括输出目录和排除的依赖项