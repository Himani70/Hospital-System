DROP PROCEDURE IF EXISTS GetPriorityStatus;
DELIMITER $$
CREATE PROCEDURE GetPriorityStatus(IN patientStatusId INT(11), OUT priorityStatusId INT(11))
DETERMINISTIC
BEGIN
    DECLARE done INTEGER DEFAULT FALSE;
    DECLARE ps INT(11);
 	DECLARE count_high INT(11) DEFAULT 0;
 	DECLARE count_normal INT(11) DEFAULT 0;
 	DECLARE count_quarantine INT(11) DEFAULT 0;
    
    DECLARE psCursor
        CURSOR FOR 
            SELECT ru.priority_status FROM patient_status ps, symptom_detail sd, symptom_body_part sbp,scale sc, rule ru,priority pr WHERE ps.patient_status_id = sd.patient_status_id AND sd.sym_id = sc.sym_id AND sd.part_id = sbp.part_id AND sd.severity_scale = sc.scale_id AND sc.svr_id = ru.svr_id AND sbp.part_id = ru.part_id AND sc.sym_id = ru.sym_id AND ru.priority_status=pr.id AND ps.patient_status_id = patientStatusId and ps.is_checkout = 0;
 

    DECLARE CONTINUE HANDLER 
        FOR NOT FOUND SET done = TRUE;
 
    OPEN psCursor;
 
    Fetch_rows: LOOP
        FETCH psCursor INTO ps;
        
        IF done THEN 
            LEAVE Fetch_rows;
        END IF;
        
        IF ps = 1 THEN 
        	SET count_high = count_high + 1;
        ELSEIF ps = 2 THEN 
        	SET count_normal = count_normal + 1;
        ELSEIF ps = 3 THEN
        	SET count_quarantine = count_quarantine + 1;	
		END IF;
    END LOOP Fetch_rows;
    
    IF count_quarantine > 0 THEN
    	SET priorityStatusId = 3;
    ELSEIF count_high > 0 THEN
    	SET priorityStatusId = 1;
    ELSE 
    	SET priorityStatusId = 2;
    END IF;	
    CLOSE psCursor;
    
END $$
DELIMITER ;


DROP TRIGGER IF EXISTS update_priority_status;
DELIMITER //
CREATE TRIGGER update_priority_status BEFORE UPDATE ON patient_status
FOR EACH ROW
BEGIN
	DECLARE priorityStatusId INT(11);
	DECLARE patientStatusId INT(11) DEFAULT 0;
	DECLARE ps INT(11) DEFAULT 0;
	IF (OLD.low_bp <> NEW.low_bp || OLD.high_bp <> NEW.high_bp || OLD.temperature <> NEW.temperature) THEN 
		CALL GetPriorityStatus(patientStatusId,@pS);
		SELECT @pS INTO priorityStatusId;
		SET NEW.priority_status_id = priorityStatusId;
	END IF;
END;
//
DELIMITER ;     


