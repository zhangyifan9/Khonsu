DELETE FROM GlobalVariables WHERE Name='Timeout';

INSERT INTO GlobalVariables(Name, Value) VALUES ('Timeout', '20');