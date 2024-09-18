package demo.local.service.wrapper;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.ServiceWrapper;
import com.liferay.portal.kernel.service.UserLocalServiceWrapper;

import org.osgi.service.component.annotations.Component;

/**
 * @author Drew Brokke
 */
@Component(property = {}, service = ServiceWrapper.class)
public class DemoUserLocalServiceWrapper extends UserLocalServiceWrapper {

	public DemoUserLocalServiceWrapper() {
		super(null);
	}

	@Override
	public User getUser(long userId) throws PortalException {
		_log.warn("");
		_log.warn(String.format("Getting user with ID: %s%n", userId));
		_log.warn("");

		User user = super.getUser(userId);

		user.setComments(
			"Hello DemoUserLocalServiceWrapper! userId: " + userId);

		return user;
	}

	private static final Log _log = LogFactoryUtil.getLog(
		DemoUserLocalServiceWrapper.class);

}