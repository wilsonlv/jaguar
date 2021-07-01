package org.jaguar.cloud.upms.sdk.dto;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.*;

/**
 * @author lvws
 * @since 2021/4/5
 */
@Data
public class MenuFunction implements Cloneable, Serializable {

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


    private static void mapChildren(List<MenuFunction> menuFunctions) {
        for (MenuFunction menuFunction : menuFunctions) {
            NAME_MAP_MENUFUNCTION.put(menuFunction.getName(), menuFunction);

            mapChildren(menuFunction.getChildren());
        }
    }

    public static boolean hasName(String name) {
        return NAME_MAP_MENUFUNCTION.containsKey(name);
    }

    public static MenuFunction getMenuFunction(String name) {
        return NAME_MAP_MENUFUNCTION.get(name);
    }

    public static List<MenuFunction> filterMenuFunctions(Set<String> names) {
        return filterMenuFunctions(MENU_FUNCTIONS, names);
    }

    @SneakyThrows
    private static List<MenuFunction> filterMenuFunctions(List<MenuFunction> menuFunctions, Set<String> names) {
        List<MenuFunction> filterMenuFunctions = new ArrayList<>();
        for (MenuFunction menuFunction : menuFunctions) {
            if (names.contains(menuFunction.getName())) {
                MenuFunction copy = new MenuFunction();
                BeanUtils.copyProperties(menuFunction, copy);
                filterMenuFunctions.add(copy);

                copy.setChildren(filterMenuFunctions(copy.getChildren(), names));
            }
        }
        return filterMenuFunctions;
    }

}
