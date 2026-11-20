package com.onarc.app.data;

import org.apache.commons.lang3.StringUtils;

// This file is mothballed, for now.  Do not treat it as a real file.
/*public class IdSystem {

     private enum TypeTag {
          ARC,
          DIAL
     }

     private enum CreatorTag {
          USER,
          SYSTEM
     }

     public static List<String> nextAvailableDialNumber() {

     }

    public String dialIdGenerator(String objectType, String creator, String dialNumber) {
        if (StringUtils.isBlank(objectType) || StringUtils.isBlank(creator) || StringUtils.isBlank(dialNumber)) {
            return null;
        }

        StringBuilder dialId = new StringBuilder();
        dialId.append(objectType);
        dialId.append(creator);
        dialId.append(dialNumber);
        return dialId.toString();
    }

     public String arcIdGenerator(String objectType, String creator, String dialNumber,  String arcNumber) {
          if (StringUtils.isBlank(objectType) || StringUtils.isBlank(creator) || StringUtils.isBlank(dialNumber) || StringUtils.isBlank(arcNumber)) {
               return null;
          }

          StringBuilder arcId = new StringBuilder();
          arcId.append(objectType);
          arcId.append(creator);
          arcId.append(dialNumber);
          arcId.append(arcNumber);
          return arcId.toString();
     }
}*/