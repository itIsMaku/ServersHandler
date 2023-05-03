# ServersHandler 2.0 [![](https://jitpack.io/v/itIsMaku/ServersHandler.svg)](https://jitpack.io/#itIsMaku/ServersHandler) ![release](https://img.shields.io/github/v/release/itIsMaku/ServersHandler)
A complete rewrite of a 2 years old project.

## Example
```java
public class Example extends JavaPlugin {

    private ServersHandler serversHandler;

    @Override
    public void onEnable() {
        serversHandler = ServersHandler.initialize(this, "Lobby", new JedisPoolBuilder()
                .withDefaultConfig()
                .hostname("localhost")
                .port(6379)
        );

        serversHandler.registerListener(synchronizedServer -> {
            System.out.printf(
                    "Server %s was updated! (%s/%s)%n", 
                    synchronizedServer.getName(), 
                    synchronizedServer.getOnlinePlayers(), 
                    synchronizedServer.getMaxPlayers()
            );

            Map<String, Object> data = synchronizedServer.getData();
            System.out.println("value: " + data.get("key"));
        });

        Map<String, SynchronizedServer> servers = serversHandler.getServers();
        System.out.println("Servers: " + servers.size());
        
        SynchronizedServer local = serversHandler.local();
        System.out.println("Local server: " + local.getName());
        
        SynchronizedServer server = serversHandler.getServers().get("Lobby");
        System.out.println("Lobby players: " + server.getOnlinePlayers());
    }

    @Override
    public void onDisable() {
        serversHandler.shutdown();
    }
    
    public void setKeyValue() {
        SynchronizedServer local = serversHandler.local();
        local.getData().put("key", "value");
        serversHandler.publish(local);
    }
}
```

## Adding to your project
### Gradle
```gradle
repositories {
  maven { url 'https://jitpack.io' }
}
```
```gradle
dependencies {
  implementation 'com.github.itIsMaku:ServersHandler:2.0.1'
}
```


### Maven
```xml
<repository>
  <id>jitpack.io</id>
  <url>https://jitpack.io</url>
</repository>
```
```xml
<dependency>
  <groupId>com.github.itIsMaku</groupId>
  <artifactId>ServersHandler</artifactId>
  <version>2.0.1</version>
</dependency>
```
