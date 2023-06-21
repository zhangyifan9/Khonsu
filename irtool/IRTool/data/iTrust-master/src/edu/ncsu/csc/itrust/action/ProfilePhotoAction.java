package edu.ncsu.csc.itrust.action;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import edu.ncsu.csc.itrust.dao.DAOFactory;
import edu.ncsu.csc.itrust.dao.mysql.ProfilePhotoDAO;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.iTrustException;

public class ProfilePhotoAction {

	private final long maxFileSize = 2048*1024; // maximum file size for uploads in bytes
	
	private long loggedInMID;
	private ProfilePhotoDAO photoDAO;
	private ServletFileUpload upload;

	public ProfilePhotoAction(DAOFactory factory, long loggedInMID) {
		this.photoDAO = factory.getProfilePhotoDAO();
		this.loggedInMID = loggedInMID;
		upload = new ServletFileUpload(new DiskFileItemFactory());
	}

	// For unit testing purposes
	public ProfilePhotoAction(DAOFactory factory, long loggedInMID, ServletFileUpload upload) {
		this.photoDAO = factory.getProfilePhotoDAO();
		this.loggedInMID = loggedInMID;
		this.upload = upload;
	}

	/**
	 * Processes a multipart reqeuest (i.e. uploads) and then parses it as an image, then storing it in the
	 * database.
	 * 
	 * @param request
	 * @return
	 * @throws DBException
	 */
	public String storePicture(HttpServletRequest request) throws DBException {
		if (ServletFileUpload.isMultipartContent(request)) {
			try {
				List<?> fileItems = upload.parseRequest(request);
				Iterator<?> iterator = fileItems.iterator();
				while (iterator.hasNext()) {
					FileItem item = (FileItem) iterator.next();
					// only process the first field anyway
					if (!item.isFormField()) {
						BufferedImage bi = ImageIO.read(item.getInputStream());
						// TODO do the validation of uploading and image size here
						if(item.getSize() > maxFileSize)
						{
							throw new iTrustException("Error uploading file - file must be 2MB or less");
						}
						photoDAO.store(loggedInMID, bi);
					}
				}
				return "Picture stored successfully";
			} catch (FileUploadException e) {
				e.printStackTrace();
				return "Error uploading file - please try again";
			} catch (IOException e) {
				e.printStackTrace();
				return "Error uploading file - please try again";
			} catch (iTrustException e) {
				e.printStackTrace();
				return e.getMessage();
			}
		} else {
			System.err.println("Not a multi-part request");
			return "Error uploading file - please try again";
		}
	}
	
	/**
	 * This method calls the ProfilePhotoDAO remove method and handles 
	 * IOExceptions appropriately.
	 * @param mid
	 * @return
	 * @throws DBException
	 */
	public String removePhoto(long mid) throws DBException
	{
		try
		{
			photoDAO.removePhoto(mid);
			return "Picture removed successfully. Now displaying default image.";
		}
		catch(IOException e)
		{
			return "Error removing file -- please try again";
		}
	}

}
