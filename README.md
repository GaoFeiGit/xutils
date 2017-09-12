本项目来源工作中的总结、网络上的一些分享，以及一些第三方的片断，为了方便使用，集成了多种常用的工具类，不断扩充，化繁为简，让很多操作尽可能变的so easy!
这个项目也是重新优化了我之前的另外一个项目，之前的项目在一些细节上还有所缺失，所以经过不断的总结，变更，就诞生了这个新的项目。
该工具类项目主要内容如下：
├─ext	依赖第三方的
│  │  CookieUtils.java Cookie操作
│  │  FreemarkerUtils.java Freemarker操作
│  │  GsonTools.java json常用的想换转换
│  │  HttpUtils.java HTTP操作
│  │  PinYinUtils.java 汉字转拼音
│  │  SimpleXmlUtils.java Bean和XML相互转换
│  │  ZipUtils.java	解压缩
│  │
│  ├─excel 表格操作
│  │      Excel.java
│  │      ExcelColor.java
│  │      ExcelFormatter.java
│  │      ExcelUtils.java
│  │
│  ├─ftp FTP，FTPS操作
│  │      FtpFile.java
│  │      FTPUtils.java
│  │
│  └─sftp	SFTP操作，注意和FTPS的区别，不是一回事
│          SFTPFile.java
│          SFTPUtils.java
│
└─j2se
    │  ArrayUtils.java 数组操作
    │  Base64Utils.java Base64转码
    │  CaptchaUtils.java 验证码
    │  CommandUtils.java 执行系统命令
    │  DateUtils.java 日期工具类
    │  DualPrivotQuicksort.java（用不到）
    │  ExceptionUtils.java 异常格式化
    │  FileUtils.java 文件各种操作
    │  GisUtils.java 地理位置，计算两点距离，某个点是否在某个区域
    │  HardwareUtils.java 硬件信息
    │  ImageUtils.java 图像操作
    │  ListUtils.java 集合操作
    │  MapUtils.java Map操作，以及转换
    │  MathUtils.java 数学，加减乘除，公约数，公倍数
    │  MD5Utils.java MD5值计算
    │  NumberConverter.java 数字转换，二进制，八进制，十进制，十六进制相互转换
    │  ObjectUtils.java 对象类型转换，复制，克隆
    │  PathUtils.java 获取路径
    │  PrintUtils.java 通过Gson转JSON格式输出
    │  PropertiesUtils.java 属性操作
    │  RandomUtils.java 随机数
    │  ReflectUtils.java 反射
    │  RegexUtils.java 正则
    │  StringUtils.java 字符串操作
    │  SystemUtils.java 获取系统属性
    │
    ├─FileWatch 监控文件、文件夹变动
    │      FileAction.java
    │      FileActionCallback.java
    │      FileWatch.java
    │      Usage.java
    │      WatchDir.java
    │
    ├─jdbc JDBC基本操作
    │      JDBCTools.java
    │
    └─mail 邮件发送，支持多附件，多收件人
            Email.java
            MailAuthenticator.java
            MailSender.java
            SmtpServer.java