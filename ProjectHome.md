# 打印色彩 #

在印刷时（如打印表格）有些颜色会因为过亮而打印在之后看不清。打印色彩函数（printable-colors）会为你刷选能打印的颜色。

刷选后一共有900种颜色（不含黑灰色）。

## 运用 ##

PrintableColors功能类会读取在presets.res文件里的可打印色彩。然后findPrintableColor函数找到最接近的颜色然后根据DARKER\_CONSTANT使改颜色变暗。如颜色为黑灰白色，色阶亮度最高改为70%。（就是说白色会打印成浅灰色，灰色会变得更深。）

通过函数findBeautifulColor（）能找到最接近的人眼能区分的色阶。在绘制表格或选择黏贴色时可以应用该函数。

![https://printable-colors.googlecode.com/files/test-white.png](https://printable-colors.googlecode.com/files/test-white.png)

![https://printable-colors.googlecode.com/files/test-lightblue.png](https://printable-colors.googlecode.com/files/test-lightblue.png)

## 演示 ##

Windows: http://printable-colors.googlecode.com/files/printable-colors-1.0.0.exe

Jar: http://printable-colors.googlecode.com/files/printable-colors-1.0.0.jar

Helper class for finding printable colors (e.g. charts and texts) on white paper.

Implementation: Some colors are too bright for printing. This helper class choose only the printable colors for the given colors. For black-white-colors the gray levels is shifted to 70% brightness.