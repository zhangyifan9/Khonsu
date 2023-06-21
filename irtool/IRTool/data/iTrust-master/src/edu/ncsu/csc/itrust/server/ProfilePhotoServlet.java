package edu.ncsu.csc.itrust.server;

import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import edu.ncsu.csc.itrust.dao.DAOFactory;
import edu.ncsu.csc.itrust.exception.iTrustException;

public class ProfilePhotoServlet extends HttpServlet {
	private static final long serialVersionUID = 180837253118457932L;
	private DAOFactory daoFactory = null;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
			IOException {
		resp.setContentType("image/jpg");
		long loggedInMID = Long.parseLong( (String) req.getSession().getAttribute("pid") );
		try {
			if (daoFactory == null) {
				daoFactory = DAOFactory.getProductionInstance();
			}
			BufferedImage bi = daoFactory.getProfilePhotoDAO().get(loggedInMID);
			ImageIO.write(bi, "jpg", resp.getOutputStream());
		} catch (iTrustException e) {
			e.printStackTrace();
		}
	}

	public void setDaoFactory(DAOFactory daoFactory) {
		this.daoFactory = daoFactory;
	}
}
