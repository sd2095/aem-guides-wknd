package com.adobe.aem.guides.wknd.core.utils;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;

import com.day.cq.dam.api.DamConstants;
import com.day.cq.dam.scene7.api.constants.Scene7Constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CommonUtils {

    /**
	 * This method return scene7 url 
	 * @param resolver
	 * @param assetPath
	 * @return scene7 url
	 */
	public static String resolveIntoScene7(ResourceResolver resolver, String assetPath) {
		if (null != resolver) {
			Resource assetResource = resolver.getResource(assetPath);
			Resource assetMetadata;
			if (null != assetResource && null != (assetMetadata = assetResource.getChild("jcr:content/metadata"))) {
				ValueMap assetProps = assetMetadata.getValueMap();
				String fileStatus = assetProps.get(Scene7Constants.PN_S7_FILE_STATUS, StringUtils.EMPTY);
				String scene7Domain = assetProps.get(Scene7Constants.PN_S7_DOMAIN, StringUtils.EMPTY);
				String scene7FileName = assetProps.get(Scene7Constants.PN_S7_FILE, StringUtils.EMPTY);
				String fileType = assetProps.get(DamConstants.DC_FORMAT, StringUtils.EMPTY);
				boolean isSmartCrop = BooleanUtils.toBoolean(assetProps.get("useSmartCropRendition", StringUtils.EMPTY));
				String smartCropParam = isSmartCrop ? "?ismartcrop=true" : StringUtils.EMPTY;
				String fileTypePath = StringUtils.containsIgnoreCase(fileType, "image") ? "is/image/" : "is/content/";

				if (StringUtils.equalsIgnoreCase(fileStatus, "publishcomplete")
						&& StringUtils.isNoneBlank(scene7Domain, scene7FileName)
						&& StringUtils.containsAnyIgnoreCase(fileType, "image", "video")) {
					return new StringBuilder(scene7Domain).append(fileTypePath).append(scene7FileName)
							.append(smartCropParam).toString();
				}
			}
		}
		return assetPath;
	}
}
