package online.cszt0.cmt.util.plat;

import online.cszt0.cmt.util.FileDialog;

/**
 * 使用 C++ 实现的在 Windows 平台上的文件选择器
 *
 * @author 初始状态0
 * @date 2019/4/20 12:10
 */
public final class FileDialogPlatWindows implements FileDialog {

    static {

        System.loadLibrary("./bin/" + (System.getProperty("sun.arch.data.model").equals("64") ? "x64/" : "") + "filedialog");

    }

    private StringBuilder filterTypes;

    public FileDialogPlatWindows() {
        filterTypes = new StringBuilder();
        addFileExtensionFilter("所有文件", "*");
    }

    @Override
    public void addFileExtensionFilter(String description, String... extensions) {
        if (extensions.length == 0) {
            throw new IllegalArgumentException("length of extensions is 0.");
        }
        filterTypes.append(description);
        filterTypes.append('(');
        for (String extension : extensions) {
            filterTypes.append("*.");
            filterTypes.append(extension);
            filterTypes.append(';');
        }
        filterTypes.deleteCharAt(filterTypes.length() - 1);
        filterTypes.append(')');
        filterTypes.append('\n');
        for (String extension : extensions) {
            filterTypes.append("*.");
            filterTypes.append(extension);
            filterTypes.append(';');
        }
        filterTypes.deleteCharAt(filterTypes.length() - 1);
        filterTypes.append('\n');
    }

    @Override
    public String showOpenDialog() {
        return showFileOpenDialog(filterTypes.toString() + '\n');
    }

    @Override
    public String showSaveDialog() {
        return showFileSaveDialog(filterTypes.toString() + '\n');
    }

    private static native String showFileOpenDialog(String filterTypes);

    private static native String showFileSaveDialog(String filerTypes);
}
