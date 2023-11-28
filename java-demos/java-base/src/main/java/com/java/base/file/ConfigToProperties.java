package com.java.base.file;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * @author : wh
 * @date : 2023/11/27 10:43
 * @description:
 */
public class ConfigToProperties {

    private static final String CONFIG = "/config";

    public static void main(String[] args) {
        String specifiedConfigPath = System.getProperty(CONFIG);
    }

    private static File findPath() throws  Exception{
        String classResourcePath = ConfigToProperties.class.getName().replaceAll("\\.", "/") + ".class";

        URL resource = ClassLoader.getSystemClassLoader().getResource(classResourcePath);
        if (resource != null) {
            String urlString = resource.toString();
            System.out.printf("The beacon class location is %s.", urlString);
            int insidePathIndex = urlString.indexOf('!');
            boolean isInJar = insidePathIndex > -1;

            if (isInJar) {
                urlString = urlString.substring(urlString.indexOf("file:"), insidePathIndex);
                File agentJarFile = null;
                try {
                    agentJarFile = new File(new URL(urlString).toURI());
                } catch (MalformedURLException | URISyntaxException e) {
                    System.out.printf("Can not locate agent jar file by url: %s.", urlString);
                }
                if (agentJarFile.exists()) {
                    return agentJarFile.getParentFile();
                }
            } else {
                int prefixLength = "file:".length();
                String classLocation = urlString.substring(
                    prefixLength, urlString.length() - classResourcePath.length());
                return new File(classLocation);
            }
        }
        System.out.printf("Can not locate agent jar file.");
        throw new Exception("Can not locate agent jar file.");
    }
}
