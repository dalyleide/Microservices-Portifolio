package br.com.sousa.util.converter;


import com.github.dozermapper.core.DozerBeanMapperBuilder;
import com.github.dozermapper.core.Mapper;

import java.util.ArrayList;
import java.util.List;

public class DozerConverter {

    private DozerConverter(){}

    private static final Mapper mapper = DozerBeanMapperBuilder.buildDefault();
    public static <O, D> D parseObject(O origin, Class<D> destination){
        return mapper.map(origin, destination);
    }
    public static <O, D> List<D> parseListObject(List<O> origins, Class<D> destination){
        List<D> destinations = new ArrayList<>();
        for (O origin : origins) {
            destinations.add(mapper.map(origin, destination));
        }
        return destinations;
    }
}
