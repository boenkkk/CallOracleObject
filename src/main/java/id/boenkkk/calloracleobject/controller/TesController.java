package id.boenkkk.calloracleobject.controller;

import id.boenkkk.calloracleobject.model.TesModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.web.bind.annotation.*;

import java.sql.Types;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/test")
public class TesController {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcCall simpleJdbcCall;

    //curl --location --request GET "http://localhost:8089/test/tesQuery
    @GetMapping("/tesQuery")
    public ResponseEntity<List<TesModel>> tesQuery(){
        try{
            List<TesModel> model = jdbcTemplate.query(
                    "select * from SCHEMA_NAME.TABLE_NAME where rownum <= 10",
                    (rs, rownum) -> new TesModel(
                            rs.getString("userid"),
                            rs.getString("groupid"),
                            rs.getString("nama"),
                            rs.getString("tgl_proses"),
                            rs.getString("aktivitas_proses"),
                            rs.getString("jumlah_data"),
                            rs.getString("keterangan")
                    )
            );

            for (TesModel data : model){
                System.out.println(data.getUserid()+"|"+data.getGroupid()+"|"+data.getNama()+"|"+data.getTgl_proses()+"|"+data.getTgl_proses()+"|"+data.getAktivitas_proses()+"|"+data.getJumlah_data()+"|"+data.getKeterangan());
            }

            return new ResponseEntity<>(model, HttpStatus.OK);
        } catch (Exception ex){
            ex.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //curl --location --request POST "http://localhost:8089/test/tesObja" --header "Content-Type: application/json" --data-raw "{\"tglAwal\": \"20201028\",\"tglAkhir\": \"20201031\"}"
    @PostMapping("/tesObja")
    public ResponseEntity<Map> tesObja(@RequestBody Map mapParam) {
        try {
            //mapParam.forEach((key, value)-> System.out.println(key+"|"+value));
            //System.out.println(mapParam.get("tglAw")+"|"+mapParam.get("tglAk"));

            simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate)
                    .withSchemaName("SCHEMA_NAME")
                    .withCatalogName("PACKAGE_NAME")
                    .withFunctionName("FUNCTION_NAME")
                    .declareParameters(
                            new SqlParameter("VPRODUK", Types.VARCHAR),
                            new SqlParameter("VTG1AWAL", Types.VARCHAR),
                            new SqlParameter("VTGLAKHIR", Types.VARCHAR),
                            new SqlOutParameter("OUT_DATA", Types.REF_CURSOR)
                    );

            Map<String, Object> hm = new HashMap<String, Object>();
            hm.put("VPRODUK", "BERJALAN");
            hm.put("VTG1AWAL", mapParam.get("tglAwal"));
            hm.put("VTGLAKHIR", mapParam.get("tglAkhir"));
            hm.put("OUT_DATA", Types.REF_CURSOR);

            SqlParameterSource in = new MapSqlParameterSource().addValues(hm);
            Map simpleJdbcResult = simpleJdbcCall.execute(in);

            //simpleJdbcResult.forEach((key, value)-> System.out.println(key+"|"+value));

            System.out.println(simpleJdbcResult.get("OUT_DATA"));

            return new ResponseEntity<>(simpleJdbcResult, HttpStatus.OK);
        } catch (Exception ex){
            ex.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //curl --location --request POST "http://localhost:8089/test/tesObjb"
    @PostMapping("/tesObjb")
    public ResponseEntity<Map> tesObjb() {
        try {
            simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate)
                    .withSchemaName("SCHEMA_NAME")
                    .withCatalogName("PACKAGE_NAME")
                    .withFunctionName("FUNCTION_NAME")
                    .declareParameters(
                            new SqlOutParameter("OUT_DATA", Types.REF_CURSOR)
                    );
            //.withoutProcedureColumnMetaDataAccess();
            //simpleJdbcCall.setAccessCallParameterMetaData(false);

            Map<String, Object> hm = new HashMap<String, Object>();
            hm.put("OUT_DATA", Types.REF_CURSOR);

            SqlParameterSource in = new MapSqlParameterSource().addValues(hm);
            Map simpleJdbcResult = simpleJdbcCall.execute(in);

            //simpleJdbcResult.forEach((key, value)-> System.out.println(key+"|"+value));

            System.out.println(simpleJdbcResult.get("OUT_DATA"));

            return new ResponseEntity<>(simpleJdbcResult, HttpStatus.OK);
        } catch (Exception ex){
            ex.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //curl --location --request POST "http://localhost:8089/test/tesObjc
    @PostMapping("/tesObjc")
    public ResponseEntity<Map> tesObjc() {
        try {
            simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate)
                    .withSchemaName("SCHEMA_NAME")
                    .withCatalogName("PACKAGE_NAME")
                    .withProcedureName("PROCEDURE_NAME")
                    .declareParameters(
                            new SqlOutParameter("PCURSOR", Types.REF_CURSOR)
                    );

            Map<String, Object> hm = new HashMap<String, Object>();
            hm.put("PCURSOR", Types.REF_CURSOR);

            SqlParameterSource in = new MapSqlParameterSource().addValues(hm);
            Map simpleJdbcResult = simpleJdbcCall.execute(in);

            simpleJdbcResult.forEach((key, value)-> System.out.println(key+"|"+value));

            System.out.println(simpleJdbcResult.get("PCURSOR"));

            return new ResponseEntity<>(simpleJdbcResult, HttpStatus.OK);
        } catch (Exception ex){
            ex.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
