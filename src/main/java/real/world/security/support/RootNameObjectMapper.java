package real.world.security.support;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class RootNameObjectMapper extends ObjectMapper {

    public RootNameObjectMapper() {
        configure(SerializationFeature.WRAP_ROOT_VALUE, true);
        configure(DeserializationFeature.UNWRAP_ROOT_VALUE, true);
    }

}