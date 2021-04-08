package org.jaguar.modules.system.mgm.dto;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import org.apache.commons.io.FileUtils;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lvws
 * @since 2021/4/5
 */
@Data
public class MenuFunction {

    public static final List<MenuFunction> MENU_FUNCTIONS;

    private static final Map<String, MenuFunction> NAME_MAP_MENUFUNCTION = new HashMap<>();

    static {
        try {
            File file = ResourceUtils.getFile("classpath:menuFunction.json");
            String jsonStr = FileUtils.readFileToString(file);
            MENU_FUNCTIONS = Collections.unmodifiableList(JSONObject.parseArray(jsonStr, MenuFunction.class));
        } catch (IOException e) {
            throw new Error(e);
        }

        mapChildren(MENU_FUNCTIONS);
    }

    private String type;
    private String name;
    private String icon;
    private String page;
    private String permission;
    private List<MenuFunction> children;


    public static boolean hasName(String name) {
        return NAME_MAP_MENUFUNCTION.containsKey(name);
    }

    public static MenuFunction getMenuFunction(String name) {
        return NAME_MAP_MENUFUNCTION.get(name);
    }

    private static void mapChildren(List<MenuFunction> menuFunctions) {
        for (MenuFunction menuFunction : menuFunctions) {
            NAME_MAP_MENUFUNCTION.put(menuFunction.getName(), menuFunction);

            mapChildren(menuFunction.getChildren());
        }
    }

}
