# CS211-652 Project aeiou

## รายชื่อสมาชิก
* 6410450800 ชนาวุฒิ วุฑฒินันท์
  * ออกแบบ GUI Staff
  * ออกแบบ staff วัสดุ(controller,models,datasource)
  * ออกแบบ theme

* 6410401086 ธราดล ลั่นซ้าย
  * ออกแบบ GUI login + credits
  * ออกแบบ credits
  * ออกแบบ register
  * ออกแบบ change password
  * ออกแบบ admin(controller,models,datasource)
  * ออกแบบ Account,AccountList models
  * แก้ไขหน้า GUI ให้สบายตามากขึ้น
  * ออกแบบ pdf คู่มือการใช้

* 6410451326 ภูริพัฒน์ พรลี้เจริญ
  * ออกแบบ GUI Admin
  * ออกแบบ GUI User 
  * ออกแบบ staff ครุภัณฑ์(controller,models,datasource)
  * ออกแบบ user

## วิธีการติดตั้งหรือรันโปรแกรม
* สามารถเปิดโปรแกรมผ่านทาง jar ไฟล์ ได้โดยตรง ที่ directory Release

## การวางโครงสร้างไฟล์
* โฟลเดอร์ data 
  * โฟลเดอร์ csv เก็บไฟล์ .csv ทั้งหมด 
    * addMateriaสsHistory.csv เก็บประวติการเพิ่มจำนวนวัสดุของแต่ละวัสดุ
    * userData.csv เก็บข้อมูลของผู้ใช้ระบบ
    * assets.csv เก็บข้อมูลรายการครุภัณฑ์ที่จะแสดงในส่วนกลาง
    * lendAssets.csv เก็บข้อมูลเกี่ยวกับการขอยืมหรือคืนรายการครุภัณฑ์ซึ่งแสดงในส่วนคำร้องของเจ้าหน้าที่
    * Materials.csv เก็บข้อมูลรายการวัสดุที่จะแสดงในส่วนกลาง
    * requestMaterials.csv เก็บประวติการเบิกจำนวนวัสดุของแต่ละวัสดุ
  * โฟลเดอร์ profileUsers จะเก็บรูปโปรไฟล์ของผู้ใช้งาน รวมถึง default profile สำหรับรูปโฟล์ไฟล์ตั้งต้น
* โฟลเดอร์ images เก็บรูปภาพทั้งหมดของครุภัณฑ์

## ตัวอย่างข้อมูลผู้ใช้ระบบ
* (User) (Username: Earthprp) (Password: Earthprp1326)
* (User) (Username: Aomtrd) (Password: Aomtrd1086)
* (User) (Username: Aucnw) (Password: Aucnw0800)
* (Staff) (Username: Staffaeiou1) (Password: AeiouStaff1)
* (Staff) (Username: Staffaeiou2) (Password: AeiouStaff2)
* (Staff) (Username: Staffaeiou3) (Password: AeiouStaff3)
* (Admin) (Username: admin ) (Password: admin)

## สรุปสิ่งที่พัฒนาแต่ละครั้งที่นำเสนอความก้าวหน้าของระบบ
* ครั้งที่ 1 (13 มกราคม 2566)
  * ออกแบบ GUI login + credits **(TharadonL)**
  * ออกแบบ GUI Staff **(Chanawut)**
  * ออกแบบ GUI Admin **(Earthprp)**
* ครั้งที่ 2 (3 กุมภาพันธ์ 2566)
  * ออกแบบ GUI User + create staff ครุภัณฑ์(controller,models,datasource) **(Earthprp)**
  * ออกแบบ theme + create staff วัสดุ(controller,models,datasource) **(Chanawut)**
  * create service + register + create admin(controller,models,datasource) **(TharadonL)**
* ครั้งที่ 3 (24 กุมภาพันธ์ 2566)
  * แก้ไข UI staff,user,admin ให้มีความสบายขึ้น,ลดจำนวนหน้าให้กระชับขึ้น **(TharadonL)**
  * create user **(Earthprp)**
  * แก้ไข UI Login ให้มีความสมบูรณ์ **(Chanawut)**
* ครั้งที่โครงงานสมบูรณ์ (17 มีนาคม 2566)
  * ประวัติการเพิ่มและเบิกวัสดุในเจ้าหน้าที่และประวัติการเบิกวัสดุในผู้ใช้,การยืมและคืนครุภัณฑ์,แสดงผล theme ในทุกหน้า,สถานะครุภัณฑ์(ปกติ/ชำรุด/จำหน่าย/บริจาค) **(TharadonL)**
  * ค้นหาครุภัณฑ์,รายการครุภัณฑ์ในผู้ใช้,ข้อมูลครุภัณฑ์ที่สมบูรณ์ **(Earthprp)**
  * เกี่ยวกับวัสดุในเจ้าหน้าที่แบบสมบูรณ์,แก้ UI และสร้าง theme **(Chanawut)**