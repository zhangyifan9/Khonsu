package edu.ncsu.csc.itrust.dao.mysql;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;
import javax.imageio.stream.MemoryCacheImageInputStream;
import javax.imageio.stream.MemoryCacheImageOutputStream;
import javax.sql.rowset.serial.SerialBlob;
import edu.ncsu.csc.itrust.DBUtil;
import edu.ncsu.csc.itrust.dao.DAOFactory;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.iTrustException;

public class ProfilePhotoDAO {
	public static final BufferedImage DEFAULT_PROFILE_PHOTO = loadDefaultProfilePhoto();

	private static BufferedImage loadDefaultProfilePhoto() {
		try {
			InputStream stream = ProfilePhotoDAO.class.getResourceAsStream("defaultProfilePhoto.jpg");
			BufferedImage read = ImageIO.read(stream);
			stream.close();
			return read;
		} catch (IOException e) {
			System.err.println("Unable to load default profile photo from ProfilePhotoDAO. "
					+ "See following stacktrace.");
			e.printStackTrace();
			return null;
		}
	}

	private DAOFactory factory;

	public ProfilePhotoDAO(DAOFactory factory) {
		this.factory = factory;
	}

	/**
	 * Given an MID and an uploaded buffered image, store it in the database
	 * 
	 * @param mid
	 * @param photo
	 * @return
	 * @throws DBException
	 * @throws IOException
	 */
	public int store(long mid, BufferedImage photo) throws DBException, IOException {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = factory.getConnection();
			ps = conn
					.prepareStatement("INSERT INTO ProfilePhotos(MID, Photo) VALUES(?,?) ON DUPLICATE KEY UPDATE Photo=?");
			ps.setLong(1, mid);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageOutputStream ios = new MemoryCacheImageOutputStream(baos);
			ImageIO.write(photo, "jpeg", ios);
			Blob photoBlob = new SerialBlob(baos.toByteArray());
			ps.setBlob(2, photoBlob);
			ps.setBlob(3, photoBlob);
			return ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException(e);
		} finally {
			DBUtil.closeConnection(conn, ps);
		}
	}

	/**
	 * Return a profile photo for the given MID. Returns a default "No Photo Available" if
	 * 
	 * @param mid
	 * @return
	 * @throws iTrustException
	 * @throws IOException
	 */
	public BufferedImage get(long mid) throws iTrustException, IOException {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = factory.getConnection();
			ps = conn.prepareStatement("SELECT * FROM ProfilePhotos WHERE MID = ?");
			ps.setLong(1, mid);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				Blob blob = rs.getBlob("Photo");
				BufferedImage bi = ImageIO.read(new MemoryCacheImageInputStream(blob.getBinaryStream()));
				return bi;
			} else
				return DEFAULT_PROFILE_PHOTO;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException(e);
		} finally {
			DBUtil.closeConnection(conn, ps);
		}
	}
	
	/**
	 * Given an MID, remove the associated image from the database
	 * 
	 * @param mid
	 * @return
	 * @throws DBException
	 * @throws IOException
	 */
	public int removePhoto(long mid) throws DBException, IOException {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = factory.getConnection();
			ps = conn.prepareStatement("DELETE FROM ProfilePhotos WHERE mid=?");
			ps.setLong(1, mid);
			return ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException(e);
		} finally {
			DBUtil.closeConnection(conn, ps);
		}
	}

}