package online.cszt0.cmt.util;

import online.cszt0.cmt.util.plat.FileDialogPlatCommon;
import online.cszt0.cmt.util.plat.FileDialogPlatWindows;

/**
 * 文件选择器
 *
 * @author 初始状态0
 * @date 2019/4/20 16:43
 */
public interface FileDialog {
	void addFileExtensionFilter(String description, String... extensions);

	String showOpenDialog();

	String showSaveDialog();

	/**
	 * 根据平台的不同生成不同的文件选择器
	 * @return 文件选择器实例
	 * @see FileDialogPlatWindows
	 * @see FileDialogPlatCommon
	 */
	static FileDialog getPlatInstance() {
		if (System.getProperty("os.name").toLowerCase().contains("windows")) {
			return new FileDialogPlatWindows();
		} else {
			return new FileDialogPlatCommon();
		}
	}
}
