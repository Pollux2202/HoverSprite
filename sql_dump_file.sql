-- Make sure to change the schema name in application.yml or application.properties 
-- Run the project before using any insert methods
use eeet2580_group8; 
drop schema eeet2580_group8;
-- Drop table if needed
drop table farmer;
drop table email;
-- Describe Table
describe farmer;
DELETE FROM farmer
WHERE fId = 'f1';

DELETE FROM feedback where feedback.id = 1;

DELETE FROM sprayer
WHERE sId = 's1';

ALTER TABLE feedback
DROP COLUMN rating;

DELETE FROM time_slots
WHERE id = 1;
-- Insert a farmer record into the farmer table 

-- *NOTE* For the enum user_type, 0 is farmer, 1 is receptionist, 2 is sprayer
INSERT INTO farmer (fid, first_name, last_name, password, phone_number, email, address, user_type)
VALUES 
('f1', 'John', 'Doe', 'hashedpassword123', '123-456-7890', 'john.doe@gmail.com', '123 Farm Lane', 0);

-- Insert another farmer record into the farmer table
INSERT INTO farmer (fid, first_name, last_name, password, phone_number, email, address, user_type)
VALUES 
('f002', 'Jane', 'Smith','hashedpassword456', '987-654-3210', 'jane.smith@example.com', '456 Harvest Road', 0);

INSERT INTO farmer (fid, first_name, last_name, password, phone_number, email, address, user_type)
VALUES 
('f003', 'Jane', 'Doe', 'hashedpassword457', '987-654-3211', 'jane.doe@example.com', '456 Reu Road', 0);

INSERT INTO farmer (fid, first_name, last_name, password, phone_number, email, address, user_type)
VALUES 
('f004', 'Huan', 'Nguyen', 'pass', '456-555-3211', 'huan.nguyen@example.com', '9 Ly Ban', 0);

Select * from receptionist where receptionist.email = 'mai.giang@hoversprite.com';

Select * from receptionist where receptionist.rid = 'r2';

Select * from farmer where farmer.email = 'hung.truong@gmail.com';

Select * from farmer where farmer.fid = 'f2';

Select * from time_slots where time_slots.lunar_date = "2024-08-09";
Select * from time_slots ;
Select * from spray_orders;
Select * from sprayer where sprayer.sid = "s1";
Select * from sprayer;
Select * from `assigned_sprayer(s)` where `assigned_sprayer(s)`.order_id = 38;
Select * from spray_orders where spray_orders.spray_id = 15;
Select * from receptionist;
Select*from feedback where feedback.id = 1;

Select*from spray_orders where stripe_id = "cs_test_a1X0jxZJPtHbRTQJLVGCtAmtIRALk6C9FJodQyr7GhIYXeA991IKe4szmM";


Select*from notification where notification.id = 13;
-- Further Insert methods and table describing here
ALTER TABLE time_slots
DROP COLUMN session_date;

DROP table time_slots;