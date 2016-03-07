package an.dpr.cyclingresultsapi.domain;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(ResultRow.class)
public class ResultRow_ {
    
    public static volatile SingularAttribute<ResultRow, Competition> competition;   
    public static volatile SingularAttribute<ResultRow, String> rank;   

}
