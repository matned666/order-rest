package eu.mrndesign.matned.metalserwisproductionrest;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonOps {

    public static String asJsonString(final Object obj) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
