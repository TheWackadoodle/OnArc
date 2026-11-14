package com.onarc.app.data;

import org.apache.commons.lang3.StringUtils;

public class IdSystem {

    public String dialIdGenerator(String objectType, String creator, String dialNumber) {
        if (StringUtils.isBlank(objectType) || StringUtils.isBlank(creator) || StringUtils.isBlank(dialNumber)) {
            return null;
        }

        StringBuilder id = new StringBuilder();
        id.append(objectType);
        id.append(creator);
        id.append(dialNumber);
        return id.toString();
    }

     public String arcIdGenerator(String objectType, String creator, String dialNumber,  String arcNumber) {
          if (StringUtils.isBlank(objectType) || StringUtils.isBlank(creator) || StringUtils.isBlank(dialNumber) || StringUtils.isBlank(arcNumber)) {
               return null;
          }

          StringBuilder id = new StringBuilder();
          id.append(objectType);
          id.append(creator);
          id.append(dialNumber);
          id.append(arcNumber);
          return id.toString();
     }
}
