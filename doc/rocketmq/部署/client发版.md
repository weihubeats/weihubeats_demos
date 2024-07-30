
# 项目打包
```shell
 mvn -Prelease-all -DskipTests -Dspotbugs.skip=true clean install
```

```shell
mvn clean package -Dmaven.test.skip=true
mvn clean deploy -Dmaven.test.skip=true
```