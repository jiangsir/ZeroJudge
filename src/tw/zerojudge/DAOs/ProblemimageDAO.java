package tw.zerojudge.DAOs;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import tw.jiangsir.Utils.DAO.SuperDAO;
import tw.jiangsir.Utils.Exceptions.DataException;
import tw.zerojudge.Objects.Problemid;
import tw.zerojudge.Tables.Problemimage;

/**
 * @author jiangsir
 * 
 */
public class ProblemimageDAO extends SuperDAO<Problemimage> {

	public ProblemimageDAO() {
	}

	/*
	 * qx Java：图像实体类、磁盘图像文件与MySql数据库中Blob字段中存储的图像
	 * 
	 * 2011-05-23 06:31:27| 分类： Java技术 | 标签：java 图像 |字号 订阅
	 * MySQL使用Blob数据类型存储图像，java (1.6)现在支持bmp,jpg, wbmp, png, gif格式的图像。
	 * 
	 * 1 从数据库中Blob转为java程序中的Image类实体：
	 * 
	 * java.sql.Blob blob = rs.getBlob("Logo"); InputStream fin =
	 * blob.getBinaryStream(); Image im = javax.imageio.ImageIO.read(fin);
	 * 
	 * 2 从数据库中Blob转存为硬盘中的图像文件：
	 * 
	 * java.sql.Blob blob = rs.getBlob("Logo"); InputStream fin =
	 * blob.getBinaryStream(); //用文件模拟输出流 File file = new
	 * File("d:\\output.gif"); OutputStream fout = new FileOutputStream(file);
	 * //将BLOB数据写入文件 byte[] b = new byte[1024]; int len = 0; while ((len =
	 * fin.read(b)) != -1) { fout.write(b, 0, len); }
	 * 
	 * 3 将磁盘中图像文件存入数据库的BLOB字段中（使用Bytes）：
	 * 
	 * FileInputStream fis = new FileInputStream(“D:\\logo.gif”); byte[] b = new
	 * byte[65000];
	 * 
	 * 将Bytes数组转为Image：
	 * 
	 * BufferedImage imag=ImageIO.read(new ByteArrayInputStream(bytearray));
	 * 
	 * 4 将磁盘中文件转为java程序中的Image类实体
	 * 
	 * Image image = Toolkit.getDefaultToolkit().getImage("D:\\logo.gif"); 或
	 * Image image = javax.imageio.ImageIO.read(new
	 * FileInputStream("D:\\logo.gif"));
	 * 
	 * 5 将java程序中的Image类实体转为磁盘中文件
	 * 
	 * Image image;
	 * 
	 * ....... BufferedImage bufferedImage = (BufferedImage) image; try {
	 * ImageIO.write(bufferedImage, "PNG", new
	 * File("d:\\yourImageName.PNG"));//输出到 png 文件 ImageIO.write(bufferedImage,
	 * "JPEG", new File("d:\\yourImageName.JPG"));//输出到 jpg 文件
	 * ImageIO.write(bufferedImage, "gif", new
	 * File("d:\\yourImageName.GIF"));//输出到 gif 文件 ImageIO.write(bufferedImage,
	 * "BMP", new File("d:\\yourImageName.BMP"));//输出到 bmp 文件 } catch
	 * (IOException e) { e.printStackTrace(); }
	 * 
	 * 6 将java程序中的Image类实体转为Byte数值后，存入数据库Blob字段中：
	 * 
	 * 首先将Image实体转为BufferedImage，可以参考网上的程序，如
	 * http://www.a3gs.com/BookViews.asp?InfoID=3111&classID=953&InfoType=0
	 * 
	 * Image image;.......
	 * 
	 * BufferedImage bi=Converter.toBufferedImage(image);
	 * 
	 * ByteArrayOutputStream baos=new ByteArrayOutputStream(1024);
	 * ImageIO.write(bi, "jpg", baos); baos.flush(); String
	 * base64String=Base64.encode(baos.toByteArray()); baos.close(); byte[] b =
	 * Base64.decode(base64String); rs.updateBytes("Logo", b);
	 * 
	 * 7 得到屏幕中的图像：
	 * 
	 * robot = new Robot(); BufferedImage bimage = robot.createScreenCapture(new
	 * Rectangle(0, 0, 100, 100)); Image image=bimage;
	 */

	public Problemimage getImage(int id) {
		String sql = "SELECT * FROM problemimages WHERE id=" + id;
		ArrayList<Problemimage> list = this.executeQuery(sql);
		if (list.size() == 0) {
			return new Problemimage();
		}
		return list.get(0);
	}

	public ArrayList<Problemimage> getImages(Problemid problemid) {
		String sql = "SELECT * FROM problemimages WHERE problemid='"
				+ problemid + "'";
		return this.executeQuery(sql);
	}


	private synchronized ArrayList<Problemimage> executeQuery(String sql) {
		ArrayList<Problemimage> images = new ArrayList<Problemimage>();
		ResultSet rs;
		try {
			PreparedStatement pstmt = getConnection().prepareStatement(sql);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				Problemimage problemimage = new Problemimage();
				problemimage.setId(rs.getInt(1));
				problemimage.setProblemid(rs.getString(2));
				problemimage.setFilename(rs.getString(3));
				problemimage.setFiletype(rs.getString(4));
				problemimage.setFilesize(rs.getInt(5));
				problemimage.setFile(rs.getBlob(6).getBinaryStream());
				problemimage.setDescript(rs.getString(7));
				problemimage.setVisible(rs.getBoolean(8));
				images.add(problemimage);
			}
			rs.close();
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return images;
	}

	public synchronized int insert(Problemimage image) {
		String sql = "INSERT INTO problemimages(problemid, filename, filetype, filesize, "
				+ "`file`, descript, visible) VALUES (?,?,?,?,?, ?,?);";
		int id = 0;
		try {
			PreparedStatement pstmt = getConnection().prepareStatement(sql,
					Statement.RETURN_GENERATED_KEYS);
			pstmt.setString(1, image.getProblemid().toString());
			pstmt.setString(2, image.getFilename());
			pstmt.setString(3, image.getFiletype());
			pstmt.setInt(4, image.getFilesize());
			pstmt.setBinaryStream(5, image.getFile(), image.getFile()
					.available());
			pstmt.setString(6, image.getDescript());
			pstmt.setBoolean(7, image.isVisible());
			executeUpdate(pstmt);
			ResultSet rs = pstmt.getGeneratedKeys();
			rs.next();
			id = rs.getInt(1);
			rs.close();
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return id;
	}

	public synchronized int update(byte[] imagebytes, int imageid) {
		String sql = "UPDATE problemimages set `file`=? WHERE id=?;";
		int id = 0;

		try {
			PreparedStatement pstmt = getConnection().prepareStatement(sql);
			pstmt.setBytes(1, imagebytes);
			pstmt.setInt(2, imageid);
			executeUpdate(pstmt);
			ResultSet rs = pstmt.getGeneratedKeys();
			rs.next();
			id = rs.getInt(1);
			rs.close();
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DataException(e);
		}
		return id;
	}

	/*
	 * 單獨取出 Blob file
	 */
	public byte[] getFile(int imageid) throws IOException {
		String sql = "SELECT `file` FROM problemimages WHERE id=" + imageid;
		InputStream is = null;
		ByteArrayOutputStream buffer;
		ResultSet rs;
		try {
			PreparedStatement pstmt = getConnection().prepareStatement(sql);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				is = rs.getBlob(1).getBinaryStream();
			}
			buffer = new ByteArrayOutputStream();
			int nRead;
			byte[] data = new byte[1024];
			while ((nRead = is.read(data, 0, data.length)) != -1) {
				buffer.write(data, 0, nRead);
			}
			buffer.flush();
			is.close();
			rs.close();
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return buffer.toByteArray();
	}

	@Override
	public int update(Problemimage t) throws DataException {
		return 0;
	}

	@Override
	public boolean delete(int i) {
		return false;
	}

}
