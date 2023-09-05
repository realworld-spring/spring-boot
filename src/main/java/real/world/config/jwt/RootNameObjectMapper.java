package real.world.config.jwt;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class RootNameObjectMapper extends ObjectMapper {

    public static RootNameObjectMapper of() {
        RootNameObjectMapper mapper = new RootNameObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, true);
        mapper.configure(DeserializationFeature.UNWRAP_ROOT_VALUE, true);

        return mapper;
    }

}