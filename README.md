# SimpleInventories

SimpleInventories used is small library for making guis in minecraft! It's also available as plugin.

[![Build Status](https://ci.screamingsandals.org/job/SimpleInventories/badge/icon?style=flat-square)](https://ci.screamingsandals.org/job/SimpleInventories/)

Look into our wiki: https://github.com/ScreamingSandals/SimpleInventories/wiki

## How to use
1. Import maven repository
```xml
<repository>
  <id>screaming-repo</id>
  <url>https://repo.screamingsandals.org/</url>
</repository>
```
2. Include dependency
```xml
<dependency>
  <groupId>org.screamingsandals.simpleinventories</groupId>
  <artifactId>SimpleInventories-Core</artifactId>
  <version>LATEST_VERSION_HERE</version>
  <scope>compile</scope>
</dependency>
```
3. Use in code
```java

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import org.screamingsandals.simpleinventories.SimpleInventories;
import org.screamingsandals.simpleinventories.events.GenerateItemEvent;
import org.screamingsandals.simpleinventories.events.PostActionEvent;
import org.screamingsandals.simpleinventories.events.PreActionEvent;
import org.screamingsandals.simpleinventories.inventory.Options;
import org.screamingsandals.simpleinventories.listeners.InventoryListener;

public class Sample extends JavaPlugin implements Listener {

    public SimpleInventories format;

    public void onEnable() {
    
        InventoryListener.init(this); // needed for all inventories

        // here do some things to load the configuration

        ItemStack backItem = someItemStack1; // here load item for go back to parent inventory
        ItemStack pageBackItem = someItemStack2; // here load item for page back
        ItemStack pageForwardItem = someItemStack3; // here load item for page forward
        ItemStack cosmeticItem = someItemStack4; // here load cosmetics on first and last line
        
        // now load options
        Options options = new Options();
        
        options.setBackItem(backItem);
        options.setPageBackItem(pageBackItem);
        options.setPageForwardItem(pageForwardItem);
        options.setCosmeticItem(cosmeticItem);

        // now we must create gui from configuration

        format = new SimpleInventories(options);
        format.loadFromDataFolder(getDataFolder(), "myAwesomeGui.yml");

        Bukkit.getServer().getPluginManager().registerEvents(this, this); // You must register your own custom events before gui is generated

        format.generateData(); // now generate gui
    }

    @EventHandler
    public void onGeneratingItem(GenerateItemEvent event) {
        if (event.getFormat() != format) {
            return; // you should check if the format is yours
        }

        // here do some stuff on generating item with creator
    }

    @EventHandler
    public void onPreAction(PreActionEvent event) {
        if (event.getFormat() != format) {
            return; // you should check if the format is yours
        }

        if (event.isCancelled()) {
            return; // you should stop working when event is cancelled
        }

        // here you should check that player has permissions for this inventory, if not, you should cancel this event
    
    }

    @EventHandler
    public void onPostAction(PostActionEvent event) {
        if (event.getFormat() != format) {
            return; // you should check if the format is yours
        }

        if (event.isCancelled()) {
            return; // you should stop working when event is cancelled
        }

        // here you should make do some stuff when player clicks to item

    }

    public void show(Player player) {
        format.openForPlayer(player);
    }
}
```
4. Relocate package
```xml
<build>
  <plugins>
    <plugin>
      <groupId>org.apache.maven.plugins</groupId>
      <artifactId>maven-shade-plugin</artifactId>
      <executions>
        <execution>
          <phase>package</phase>
          <goals>
            <goal>shade</goal>
          </goals>
          <configuration>
            <relocations>
              <relocation>
                <pattern>org.screamingsandals.simpleinventories</pattern>
                <shadedPattern>${project.groupId}.si</shadedPattern>
              </relocation>
            </relocations>
          </configuration>
        </execution>
      </executions>
    </plugin>
  </plugins>
</build>
  ```
5. Now build your plugin and enjoy it!
`mvn install`
