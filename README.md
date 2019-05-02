# SimpleGuiFormat

Simple Gui Format used for formatting guis for Spigot minigames!

[![Build Status](https://jenkins.mtorus.cz:443/view/Minecraft%20plugins/job/SimpleGuiFormat/badge/icon?style=flat-square)](https://jenkins.mtorus.cz:443/view/Minecraft%20plugins/job/SimpleGuiFormat/)

## How to use
1. Import maven repository
```xml
<repository>
  <id>mtorus-repo</id>
  <url>https://jenkins.mtorus.cz/plugin/repository/everything/</url>
</repository>
```
2. Include dependency
```xml
<dependency>
  <groupId>misat11.lib.sgui</groupId>
  <artifactId>SimpleGuiFormat</artifactId>
  <version>0.0.2</version>
  <scope>compile</scope>
</dependency>
```
3. Use in code
```java

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import misat11.lib.sgui.SimpleGuiFormat;
import misat11.lib.sgui.StaticGuiCreator;
import misat11.lib.sgui.StaticInventoryListener;
import misat11.lib.sgui.events.GenerateItemEvent;
import misat11.lib.sgui.events.PostActionEvent;
import misat11.lib.sgui.events.PreActionEvent;

public class Sample extends JavaPlugin implements Listener {

    public SimpleGuiFormat format;
    public StaticGuiCreator creator;

    public void onEnable() {

        // here do some things to load the configuration

        ItemStack backItem = someItemStack1; // here load item for go back to parent inventory
        ItemStack pageBackItem = someItemStack2; // here load item for page back
        ItemStack pageForwardItem = someItemStack3; // here load item for page forward
        ItemStack cosmeticItem = someItemStack4; // here load cosmetics on first and last line

        List<Map<String, Object>> data = myBestConfiguration; // here load your configuration

        // now we must create gui from configuration

        format = new SimpleGuiFormat(data);
        format.generateData();

        SimpleGuiCreator creator = new StaticGuiCreator(nameOfYourInventory, format, backItem, pageBackItem, pageForwardItem, cosmeticItem);

        StaticInventoryListener listener = new StaticInventoryListener(creator);
        Bukkit.getServer().getPluginManager().registerEvents(listener, this); // Needed for navigate in inventory
        Bukkit.getServer().getPluginManager().registerEvents(this, this); // Needed for onclick action

        
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
        p.openInventory(creator.getInventories(null).get(0)); // get main inventory
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
                <pattern>misat11.lib</pattern>
                <shadedPattern>yourcustompackage.lib</shadedPattern>
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
