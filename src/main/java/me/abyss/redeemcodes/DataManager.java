package me.abyss.redeemcodes;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * RedeemCodes
 *
 * @author Abyss
 */
public class DataManager {

    private RedeemCodesMain plugin;

    private File codesFile;
    private FileConfiguration codesConfig;

    private Map<String, Code> codeMap;

    public DataManager() {
        plugin = RedeemCodesMain.getPlugin();
        codeMap = new HashMap<>();
        createAndLoadFile();
    }

    public void createAndLoadFile(){
        codesFile = new File(plugin.getDataFolder(), "codes.yml");
        if (!codesFile.exists()) {
            codesFile.getParentFile().mkdirs();
            try {
                codesFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        codesConfig = new YamlConfiguration();
        try {
            codesConfig.load(codesFile);
        } catch (InvalidConfigurationException | IOException e) {
            e.printStackTrace();
        }
        loadData();
    }

    public void loadData() {
        codesConfig.getKeys(false).forEach(key -> {
            String perm = codesConfig.getString(key+".permission", "");
            List<String> commands = codesConfig.getStringList(key+".commands");
            int usages = codesConfig.getInt(key+".usages", -1);
            List<String> players = codesConfig.getStringList(key+".players");
            codeMap.put(key, new Code(key, commands, perm, usages, players));
        });
    }

    public void saveData() {
        codeMap.forEach((code, CodeObject) -> {
            delDataConfig(code);
            codesConfig.set(code+".permission", CodeObject.getPermission());
            codesConfig.set(code+".commands", CodeObject.getCommands());
            codesConfig.set(code+".usages", CodeObject.getUsages());
            codesConfig.set(code+".players", CodeObject.getPlayerUUIDUsed());
        });
        try {
            codesConfig.save(codesFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void delDataConfig(String code) {
        codesConfig.set(code+".permission", null);
        codesConfig.set(code+".commands", null);
        codesConfig.set(code+".usages", null);
        codesConfig.set(code, null);
    }

    public Map<String, Code> getCodeMap() {
        return codeMap;
    }

    public boolean addData(Code code){
        if (codeMap.get(code.getCode()) != null)
            return false;
        codeMap.put(code.getCode(), code);
        saveData();
        return true;
    }

    public boolean delData(String code){
        codeMap.remove(code);
        saveData();
        if (codeMap.get(code) == null){
            return true;
        }
        return false;
    }

    public Code getData(String code) {
        return codeMap.get(code);
    }

}
