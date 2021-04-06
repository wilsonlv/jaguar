package org.jaguar.modules.system.mgm.dto;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import org.apache.commons.io.FileUtils;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

/**
 * @author lvws
 * @since 2021/4/5
 */
@Data
public class MenuFunction {

    public static final List<MenuFunction> MENU_FUNCTIONS;

    static {
        try {
            File file = ResourceUtils.getFile("classpath:menuFunction.json");
            String jsonStr = FileUtils.readFileToString(file);
            MENU_FUNCTIONS = Collections.unmodifiableList(JSONObject.parseArray(jsonStr, MenuFunction.class));
        } catch (IOException e) {
            throw new Error(e);
        }
    }

    private String type;
    private String name;
    private String icon;
    private String page;
    private String permission;
    private List<MenuFunction> children;


    public static boolean hasName(String name) {
        for (MenuFunction menuFunction : MENU_FUNCTIONS) {
            if (menuFunction.getName().equals(name)) {
                return true;
            } else {
                return hasName(name, menuFunction.getChildren());
            }
        }
        return false;
    }

    private static boolean hasName(String name, List<MenuFunction> menuFunctions) {
        for (MenuFunction menuFunction : menuFunctions) {
            if (menuFunction.getName().equals(name)) {
                return true;
            } else {
                return hasName(name, menuFunction.getChildren());
            }
        }

        return false;
    }

}
