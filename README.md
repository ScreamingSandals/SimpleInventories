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
import org.bukkit.Player;

import org.screamingsandals.simpleinventories.material.builder.ItemFactory;
import org.screamingsandals.simpleinventories.bukkit.SimpleInventoriesBukkit;

public class Sample extends JavaPlugin {
    
    private InventorySet inventorySet;

    public void onEnable() {

        SimpleInventoriesBukkit.init(this);

        inventorySet = SimpleInventoriesBukkit.builder()
                .render(itemRenderEvent -> {
                    
                })
                .preClick(preClickEvent -> {
                    
                })
                .click(clickEvent -> {
                    
                })
                .categoryOptions(options -> 
                    options.backItem(ItemFactory.build(someItemStack1))
                        .pageBackItem(ItemFactory.build(someItemStack2))
                        .pageForwardItem(ItemFactory.build(someItemStack3))
                        .cosmeticItem(ItemFactory.build(someItemStack4))
                )
                .include("myAwesomeGui.yml")
                .process();
    }

    public void show(Player player) {
        SimpleInventoriesBukkit.wrapPlayer(player).openInventory(inventorySet);
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
