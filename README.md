# 百度云解压工具
百度云打包下载的zip压缩包，用普通的解压工具解压无法正常解压(如果压缩包内没有超过4G大小的文件用WinRAR一般可以正常解压)，所以开发了这款专门解压百度云zip压缩包的工具。
## 下载
[releases](https://github.com/monkeyWie/bdy-unzip/releases)
## 使用
运行需要java环境,版本为1.8+
```
java -jar bdy-unpack.jar <packFile> [unpackPath]
```
- **packFile：** 待解压的压缩包
- **unpackPath：** 指定解压目录，默认为当前目录
