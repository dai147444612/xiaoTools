package com.xiaoTools.util.fileUtil.fileUtil;


import com.xiaoTools.lang.constant.Constant;
import com.xiaoTools.util.arrayUtil.ArrayUtil;
import com.xiaoTools.util.charUtil.CharUtil;

import java.io.*;

/**
 * [文件工具类](File tools)
 * @author XiaoXunYao
 * @since 2021/5/18 9:58 上午
*/
public class FileUtil {

    /**
     * [初始化方法](Initialization method)
     * @version: V1.0
     * @author XiaoXunYao
     * @since 2021/5/18 9:58 上午
    */
    public FileUtil() { }


    /**
     * [获取当前系统的换行分隔符](Gets the newline separator of the current system)
     * @description: zh - 获取当前系统的换行分隔符
     * @description: en - Gets the newline separator of the current system
     * @version: V1.0
     * @author XiaoXunYao
     * @since 2021/6/23 7:52 下午
     * @return java.lang.String
     */
    public static String getLineSeparator() {
        return System.lineSeparator();
    }

    /**
     * [通过路径查看该路径下的[文件](包括文件夹)](View [file] (including folder) under the path through the path)
     * @description: zh - 会获取该路径下的所有文件包括文件夹。
     * @description: en - Will get all the files under the path, including the folder.
     * @version: V1.0
     * @author XiaoXunYao
     * @since 2021/5/18 10:16 上午
     * @param path: [路径](route)
     * @return java.io.File[]
    */
    public static File[] ls(String path){
        if ( path == Constant.NULL ||path.length() == 0 ){
            return (File[]) Constant.NULL;
        }else {
            File file = new File(path);
            return file.listFiles();
        }
    }

    /**
     * [[创建文件](true创建成功，false创建失败)]([create file] (true created successfully, false created failed))
     * @description: zh - 输入一个文件流，将该文件进行创建操作，若没有父级目录，则先创建父级目录
     * @description: en - Enter a file stream and create the file. If there is no parent directory, create the parent directory first
     * @version: V1.0
     * @author XiaoXunYao
     * @since 2021/5/18 1:19 下午
     * @param file: [需要创建的文件](Files to be created)
     * @return boolean
    */
    public static boolean touch(File file){
        if (Constant.NULL == file){
            return Constant.FALSE;
        }else {
            //判断文件是否存在
            if (!file.exists()){
                mkdirParentDirs(file);
                //创建文件
                try {
                    if (!file.createNewFile()) {
                        return Constant.FALSE;
                    }
                } catch (Exception e) {
                    return Constant.FALSE;
                }
            }
            return Constant.TRUE;
        }
    }

    /**
     * [通过完整的路径创建文件](Create the file through the full path)
     * @description: zh - 通过完整的路径创建文件，如果没有父级目录则会创建父级目录
     * @description: en - Create the file through the full path. If there is no parent directory, the parent directory will be created
     * @version: V1.0
     * @author XiaoXunYao
     * @since 2021/5/18 3:32 下午
     * @param fullFilePath:[完整的路径](Complete path)
     * @return boolean
    */
    public static boolean touch(String fullFilePath){
        return fullFilePath != Constant.NULL && fullFilePath.length() != Constant.ZERO && touch(file(fullFilePath));
    }

    /**
     * [创建文件夹](create folder)
     * @description: zh - 在该文件流的位置下创建一个文件夹
     * @description: en - Create a folder under the location of the file stream
     * @version: V1.0
     * @author XiaoXunYao
     * @since 2021/5/18 1:36 下午
     * @param file: [目录地址](Directory address)
     * @return boolean
    */
    public static boolean mkdir(File file) {
        //判断文件是否为空
        if (file == Constant.NULL) {
            return Constant.FALSE;
        } else {
            //判断文件是否存在
            if (!file.exists()) {
                //创建文件夹
                return file.mkdirs();
            }
            return Constant.TRUE;
        }
    }

    /**
     * [删除文件](Delete file)
     * @description: zh - 通过删除文件的路径，进行删除操作
     * @description: en - Delete by deleting the path of the file
     * @version: V1.0
     * @author XiaoXunYao
     * @since 2021/5/18 2:03 下午
     * @param file: [需要删除文件的路径](The path of the file needs to be deleted)
     * @return boolean
    */
    public static boolean rm(File file){
        //判断file是否为空
        if (file != Constant.NULL && file.exists()){
            //判断该文件路径是否指向的是文件夹
            if (file.isDirectory()) {
                //清空该文件夹内的所有操作
                if (!clean(file)) {
                    return Constant.FALSE;
                }
            }
            //删除文件夹
            return file.delete();
        }else {
            return Constant.TRUE;
        }
    }

    /**
     * [清空文件夹内的文件](Empty files in folder)
     * @description: zh - 输入文件夹的地址，将会清空文件夹内的所有文件
     * @description: en - Entering the address of the folder will empty all the files in the folder
     * @version: V1.0
     * @author XiaoXunYao
     * @since 2021/5/18 2:08 下午
     * @param directory: [需要清空的文件夹路径](Folder path to be cleared)
     * @return boolean
    */
    public static boolean clean(File directory) {
        if (directory != Constant.NULL && directory.exists() && directory.isDirectory()) {
            File[] files = directory.listFiles();
            if (Constant.NULL != files) {
                for (File file : files) {
                    if (!rm(file)) {
                        return Constant.FALSE;
                    }
                }
            }
        }
        return Constant.TRUE;
    }

    /**
     * [获取文件的绝对路径](Get the absolute path of the file)
     * @description: zh - 通过文件的相对路径获取到该文件的绝对路径
     * @description: en - Get the absolute path of the file through the relative path of the file
     * @version: V1.0
     * @author XiaoXunYao
     * @since 2021/5/18 4:49 下午
     * @param path: [文件的地址](The address of the file)
     * @return java.lang.String
    */
    public static String getAbsolutePath(String path) {
        File file = new File(path);
        try {
            return file.getCanonicalPath();
        } catch (Exception e) {
            return file.getAbsolutePath();
        }
    }

    /**
     * [将源文件流中的数据复制到目标位置](Copy the data from the source file stream to the destination)
     * @description: zh - 将源文件流中的数据复制到目标位置
     * @description: en - Copy the data from the source file stream to the destination
     * @version: V1.0
     * @author XiaoXunYao
     * @since 2021/5/24 11:14 上午
     * @param resource: [需要移动的源文件](Source files that need to be moved)
     * @param target: [复制到的目标位置](Copy to destination)
     * @return boolean
    */
    public static boolean cp(File resource,File target){
        return isFile(resource) ? copyFile(resource,target) : copyFolder(resource, target);
    }

    /**
     * [将源文件流中的数据复制到目标位置](Copy the data from the source file stream to the destination)
     * @description: zh - 将源文件流中的数据复制到目标位置
     * @description: en - Copy the data from the source file stream to the destination
     * @version: V1.0
     * @author XiaoXunYao
     * @since 2021/5/24 11:21 上午
     * @param resource: [需要移动的源文件](Source files that need to be moved)
     * @param target: [复制到的目标位置](Copy to destination)
     * @return boolean
    */
    public static boolean cp(String resource,String target){
        return isFile(resource) ? copyFile(resource, target) : copyFolder(resource, target);
    }

    /**
     * [输入源文件的地址流将该文件的名字进行修改](Enter the address stream of the source file and modify the name of the file)
     * @description: zh - 输入源文件的地址流将该文件的名字进行修改
     * @description: en - Enter the address stream of the source file and modify the name of the file
     * @version: V1.0
     * @author XiaoXunYao
     * @since 2021/5/21 8:24 下午
     * @param file: [需要修改名字的文件或文件夹地址的全路径](The full path of the address of the file or folder whose name needs to be changed)
     * @param newName: [修改的新名字](Modified new name)
     * @return boolean
    */
    public static boolean rename(String file,String newName){
        File f = file(file);
        // 判断原文件是否存在（防止文件名冲突）
        if (!f.exists()) {
            return Constant.FALSE;
        }
        newName = newName.trim();
        // 文件名不能为空
        if (Constant.EMPTY.equals(newName) || newName == Constant.NULL) {
            return Constant.FALSE;
        }
        String newFilePath = file.substring(Constant.ZERO, file.lastIndexOf(Constant.SINGLE_STRING_SLASH)) + Constant.SINGLE_STRING_SLASH + newName;
        // 判断是否为文件夹
        if (!f.isDirectory()) {
            newFilePath = newFilePath + file.substring(file.lastIndexOf(Constant.SPOT));
        }
        File nf = new File(newFilePath);
        try {
            // 修改文件名
            return f.renameTo(nf);
        } catch (Exception err) {
            err.printStackTrace();
            return Constant.FALSE;
        }
    }

    /**
     * [输入需要判断的文件流，进行文件流的判断](Input the file stream to be judged and judge the file stream)
     * @description: zh - 输入需要判断的文件流，进行文件流的判断
     * @description: en -  Input the file stream to be judged and judge the file stream
     * @version: V1.0
     * @author XiaoXunYao
     * @since 2021/5/22 8:06 上午
     * @param file: [需要判断的文件流](File stream to judge)
     * @return boolean
    */
    public static boolean isDirectory(File file){
        return null != file && file.isDirectory();
    }

    /**
     * [输入文件的全路径地址，判断是否是文件夹](Enter the full path address of the file to determine whether it is a folder)
     * @description: zh - 输入文件的全路径地址，判断是否是文件夹
     * @description: en - Enter the full path address of the file to determine whether it is a folder
     * @version: V1.0
     * @author XiaoXunYao
     * @since 2021/5/22 8:10 上午
     * @param file: [需要判断的文件流全路径地址](Full path address of file stream to be judged)
     * @return boolean
    */
    public static boolean isDirectory(String file){
        return null != file && isDirectory(file(file));
    }

    /**
     * [将源文件移动到目标的位置](Move the source file to the destination location)
     * @description: zh - 将源文件移动到目标的位置
     * @description: en - Move the source file to the destination location
     * @version: V1.0
     * @author XiaoXunYao
     * @since 2021/5/21 11:00 下午
     * @param resource: [需要移动的文件或者文件夹](Files or folders that need to be moved)
     * @param target: [移动文件的目标位置](Where to move files)
     * @return boolean
    */
    public static boolean mv(File resource, File target){
        //判断源文件是否是文件
        if (isDirectory(resource)) {
            //是文件夹
            if (copyFolder(resource, target)) {
                return rm(resource);
            }
        }else {
            //是文件
            if (copyFile(resource, target)) {
                return rm(resource);
            }
        }
        return false;
    }

    /**
     * [将源文件移动到目标的位置](Move the source file to the destination location)
     * @description: zh - 将源文件移动到目标的位置
     * @description: en - Move the source file to the destination location
     * @version: V1.0
     * @author XiaoXunYao
     * @since 2021/5/22 12:35 下午
     * @param resource: [需要移动的文件或者文件夹](Files or folders that need to be moved)
     * @param target: [移动文件的目标位置](Where to move files)
     * @return boolean
    */
    public static boolean mv(String resource, String target){
        return mv(file(resource),file(target));
    }

    /**
     * [输入文件流获取到文件的名字](Enter the file stream to get the name of the file)
     * @description: zh - 输入文件流获取到文件的名字
     * @description: en - Enter the file stream to get the name of the file
     * @version: V1.0
     * @author XiaoXunYao
     * @since 2021/5/23 3:10 下午
     * @param file: [需要获取文件名字的文件流](Need to get the file stream of the file name)
     * @return java.lang.String
    */
    public static String getName(File file){
        return null != file ? file.getName() : Constant.STRING_NULL;
    }

    /**
     * [输入文件的地址，获取到文件的名字](Enter the address of the file to get the name of the file)
     * @description: zh - 输入文件的地址，获取到文件的名字
     * @description: en - Enter the address of the file to get the name of the file
     * @version: V1.0
     * @author XiaoXunYao
     * @since 2021/5/23 3:21 下午
     * @param filePath: [文件的地址](The address of the file)
     * @return java.lang.String
    */
    public static String getName(String filePath){
        if (Constant.NULL == filePath) {
            return null;
        } else {
            int len = filePath.length();
            if (Constant.ZERO == len) {
                return filePath;
            } else {
                if (CharUtil.isFileSeparator(filePath.charAt(len - Constant.NEGATIVE_ONE))) {
                    --len;
                }
                int begin = Constant.ZERO;
                for(int i = len - Constant.ONE; i > Constant.NEGATIVE_ONE; --i) {
                    char c = filePath.charAt(i);
                    if (CharUtil.isFileSeparator(c)) {
                        begin = i + Constant.ONE;
                        break;
                    }
                }
                return filePath.substring(begin, len);
            }
        }
    }

    /**
     * [输入文件流，判断该文件流是否为空](Input file stream to judge whether the file stream is empty)
     * @description: zh - 输入文件流，判断该文件流是否为空
     * @description: en - Input file stream to judge whether the file stream is empty
     * @version: V1.0
     * @author XiaoXunYao
     * @since 2021/5/23 7:37 下午
     * @param file: 需要判断是否为空的文件流
     * @return boolean
    */
    public static boolean isEmpty(File file){
        if (null == file) {
            return Constant.TRUE;
        } else if (file.isDirectory()) {
            String[] subFiles = file.list();
            return ArrayUtil.isEmpty(subFiles);
        } else if (file.isFile()) {
            return file.length() <= 0L;
        } else {
            return Constant.FALSE;
        }
    }

    /**
     * [输入文件流判断是否是文件](Input file stream to judge whether it is a file)
     * @description: zh - 输入文件流判断是否是文件
     * @description: en - Input file stream to judge whether it is a file
     * @version: V1.0
     * @author XiaoXunYao
     * @since 2021/5/24 8:43 上午
     * @param file: [需要判断是否为文件的文件流](Need to determine whether it is the file stream of the file)
     * @return boolean
    */
    public static boolean isFile(File file){
        return Constant.NULL != file && file.isFile();
    }

    /**
     * [输入文件流的绝对路径判断是否是文件](Enter the absolute path of the file stream to determine whether it is a file)
     * @description: zh - 输入文件流的绝对路径判断是否是文件
     * @description: en - Enter the absolute path of the file stream to determine whether it is a file
     * @version: V1.0
     * @author XiaoXunYao
     * @since 2021/5/24 8:51 上午
     * @param filePath: [需要判断是否是文件的文件流](Need to determine whether it is the file stream of the file)
     * @return boolean
    */
    public static boolean isFile(String filePath){
        return Constant.NULL != filePath && file(getAbsolutePath(filePath)).isFile();
    }

    /**
     * [输入所需要获取的文件流，获取该文件流的后缀](Input the file stream to get and get the suffix of the file stream)
     * @description: zh - 输入所需要获取的文件流，获取该文件流的后缀
     * @description: en - Input the file stream to get and get the suffix of the file stream
     * @version: V1.0
     * @author XiaoXunYao
     * @since 2021/5/24 9:11 上午
     * @param file: [文件流](File stream)
     * @return java.lang.String
     */
    public static String getSuffix(File file){
        if (file == Constant.NULL){
            return Constant.STRING_NULL;
        }else {
            return file.isDirectory() ? Constant.STRING_NULL : getSuffix(file.getName());
        }
    }

    /**
     * [输入所需要获取的文件流，获取该文件流的后缀](Input the file stream to get and get the suffix of the file stream)
     * @description: zh - 输入所需要获取的文件流，获取该文件流的后缀
     * @description: en - Input the file stream to get and get the suffix of the file stream
     * @version: V1.0
     * @author XiaoXunYao
     * @since 2021/5/24 9:11 上午
     * @param fileName: [文件的名字](The name of the file)
     * @return java.lang.String
    */
    public static String getSuffix(String fileName){
        if (fileName == Constant.NULL) {
            return Constant.STRING_NULL;
        } else {
            int index = fileName.lastIndexOf(Constant.SPOT);
            if (index == Constant.NEGATIVE_ONE) {
                return Constant.EMPTY;
            } else {
                String ext = fileName.substring(index + Constant.ONE);
                return StrUtil.containsAny(ext, Constant.SINGLE_CHAR_SLASH, Constant.DOUBLE_CHAR_SLASH) ? Constant.EMPTY : ext;
            }
        }
    }

    /**
     * [创建父级目录](Create parent directory)
     * @description: zh - 将路径上的文件夹进行创建操作
     * @description: en - Create the folder on the path
     * @version: V1.0
     * @author XiaoXunYao
     * @since 2021/5/18 1:27 下午
     * @param file: [父级目录是否存在](Does the parent directory exist)
     */
    private static void mkdirParentDirs(File file) {
        if (null != file) {
            mkdir(file.getParentFile());
        }
    }

    /**
     * [通过路径实现文件](Implement file by path)
     * @description: zh - 通过文件路径创建文件流
     * @description: en - Creating a file stream through a file path
     * @version: V1.0
     * @author XiaoXunYao
     * @since 2021/5/18 3:39 下午
     * @param path: [文件的位置路径](The location path of the file)
     * @return java.io.File
    */
    private static File file(String path) {
        return Constant.NULL == path ? Constant.FILE_NULL : new File(getAbsolutePath(path));
    }

    /**
     * [复制文件，将所需要复制的文件复制到目标的目录](Copy file, copy the file to the target directory)
     * @description: zh - 输入源文件的地址和目标文件的地址进行文件的复制
     * @description: en - Input the address of the source file and the address of the target file to copy the file
     * @version: V1.0
     * @author XiaoXunYao
     * @since 2021/5/20 8:09 下午
     * @param source: 源文件的地址
     * @param target: 目标文件的地址
     * @return boolean
     */
    private static boolean copyFile(String source,String target){
        try {
            int byteSum = Constant.ZERO;
            int byteRead;
            File oldFile = new File(source);
            //文件存在时
            if (oldFile.exists()) {
                //读入原文件
                InputStream inStream = new FileInputStream(source);
                FileOutputStream fs = new FileOutputStream(target);
                byte[] buffer = new byte[Constant.FILE_BYTE];
                while ((byteRead = inStream.read(buffer)) != Constant.NEGATIVE_ONE) {
                    //字节数 文件大小
                    byteSum += byteRead;
                    fs.write(buffer, Constant.ZERO, byteRead);
                }
                inStream.close();
                return Constant.TRUE;
            }
        } catch (Exception e) {
            return Constant.FALSE;
        }
        return Constant.TRUE;
    }

    /**
     * [将源文件流的文件复制到目标的文件](Copy files from the source file stream to the destination file)
     * @description: zh - 输入源文件流和目标文件流，将源文件复制到目标文件处
     * @description: en - Input the source file stream and the target file stream to copy the source file to the target file
     * @version: V1.0
     * @author XiaoXunYao
     * @since 2021/5/21 7:49 上午
     * @param resource: [源文件的文件流](The file stream of the source file)
     * @param target: [目标文件的文件流](The file stream of the target file)
     * @return boolean
     */
    private static boolean copyFile(File resource, File target){
        try {
            // 输入流 --> 从一个目标读取数据
            // 输出流 --> 向一个目标写入数据
            // 文件输入流并进行缓冲
            FileInputStream inputStream = new FileInputStream(resource);
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);

            // 文件输出流并进行缓冲
            FileOutputStream outputStream = new FileOutputStream(target);
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream);

            // 缓冲数组
            // 大文件 可将 1024 * 2 改大一些，但是 并不是越大就越快
            byte[] bytes = new byte[Constant.ONE_MEGA_BYTES * Constant.TWO];
            int len = Constant.ZERO;
            while ((len = inputStream.read(bytes)) != Constant.NEGATIVE_ONE) {
                bufferedOutputStream.write(bytes, Constant.ZERO, len);
            }
            // 刷新输出缓冲流
            bufferedOutputStream.flush();
            //关闭流
            bufferedInputStream.close();
            bufferedOutputStream.close();
            inputStream.close();
            outputStream.close();
            return Constant.TRUE;
        }catch (Exception e){
            return Constant.FALSE;
        }
    }

    /**
     * [输入源文件夹地址和目标文件夹地址，将源文件夹地址中的所有文件复制到目标地址文件，若目标地址并未指向已经存在的文件夹，则先创建文件夹，在进行复制。](Input the address of the source folder and the address of the destination folder, copy all the files in the address of the source folder to the file of the destination address. If the destination address does not point to the existing folder, create the folder first, and then copy.)
     * @description: zh - 输入源文件夹流和目标文件夹流，将源文件夹中的所有文件复制到目标文件夹中。
     * @description: en - Input the source folder stream and the destination folder stream to copy all the files in the source folder to the destination folder.
     * @version: V1.0
     * @author XiaoXunYao
     * @since 2021/5/21 7:59 上午
     * @param resource: [源文件夹地址](Source folder address)
     * @param target: [目标文件夹地址](Destination folder address)
     * @return boolean
     */
    private static boolean copyFolder(String resource, String target){
        try {
            File resourceFile = file(resource);
            if (!resourceFile.exists()) { return Constant.FALSE; }
            File targetFile = file(target);
            if (!targetFile.exists()) { mkdir(targetFile); }
            // 获取源文件夹下的文件夹或文件
            for (File file : resourceFile.listFiles()) {
                File file1 = new File(targetFile.getAbsolutePath() + File.separator + resourceFile.getName());
                // 复制文件
                if (file.isFile()) {
                    // 在 目标文件夹（B） 中 新建 源文件夹（A），然后将文件复制到 A 中
                    // 这样 在 B 中 就存在 A
                    if (!file1.exists()) {
                        file1.mkdirs();
                    }
                    File targetFile1 = new File(file1.getAbsolutePath() + File.separator + file.getName());
                    copyFile(file, targetFile1);
                }
                // 复制文件夹
                // 复制源文件夹
                if (file.isDirectory()) {
                    String dir1 = file.getAbsolutePath();
                    // 目的文件夹
                    String dir2 = file1.getAbsolutePath();
                    copyFolder(dir1, dir2);
                }
            }
        } catch (Exception e) {
            return Constant.FALSE;
        }
        return Constant.TRUE;
    }

    /**
     * [输入源文件流和目标文件流，将源文件流中的所有文件复制到目标文件处。](Input the source file stream and the target file stream, and copy all the files in the source file stream to the target file.)
     * @description: zh - 输入源文件流，将源文件流中的所有文件复制到目标文件处。
     * @description: en - Input the source file stream and copy all the files in the source file stream to the destination file.
     * @version: V1.0
     * @author XiaoXunYao
     * @since 2021/5/21 7:40 下午
     * @param resource: [源文件夹地址](Source folder address)
     * @param target: [目标文件地址](Destination file address)
     * @return boolean
     */
    private static boolean copyFolder(File resource, File target){
        try {
            if (!resource.exists()) {
                return Constant.FALSE;
            }
            if (!target.exists()) {
                mkdir(target);
            }
            //判断文件是否为空
            if (!isEmpty(resource)) {
                return Constant.FALSE;
            }
            // 获取源文件夹下的文件夹或文件
            File[] resourceFiles = resource.listFiles();
            for (File file : resourceFiles) {
                File file1 = new File(target.getAbsolutePath() + File.separator + resource.getName());
                // 复制文件
                if (file.isFile()) {
                    // 在 目标文件夹（B） 中 新建 源文件夹（A），然后将文件复制到 A 中
                    // 这样 在 B 中 就存在 A
                    if (!file1.exists()) {
                        file1.mkdirs();
                    }
                    File targetFile1 = new File(file1.getAbsolutePath() + File.separator + file.getName());
                    copyFile(file, targetFile1);
                }
                // 复制文件夹
                // 复制源文件夹
                if (file.isDirectory()) {
                    String dir1 = file.getAbsolutePath();
                    // 目的文件夹
                    String dir2 = file1.getAbsolutePath();
                    copyFolder(dir1, dir2);
                }
            }
        } catch (Exception e) {
            return Constant.FALSE;
        }
        return Constant.TRUE;
    }

}
