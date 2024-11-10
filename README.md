# HealthyState
Мобильное приложение для самоконтроля, упрощение планирования, учета действий для улучшения здорового состояния пользователей.

## Роли пользователей и функциональность приложения
В приложение присутствует единственная роль – пользователь, которая обладает следующими возможностями:
•	Регистрация;

•	Авторизация в системе;

•	Вывод списка тренеров в зависимости от выбранной даты;

•	Вывод списка таблеток в зависимости от выбранной даты;

•	Вывод списка питания в зависимости от выбранной даты;

•	Вывод индикатора прогресса воды зависимости от выбранной даты;

•	Вывод списка записей в личном дневнике;

•	Вывод списка задач;

•	Изменение пароля;

•	Изменение данных о добавленном питании;

•	Изменение данных о добавленных таблетках;

•	Изменение данных о добавленных тренировках;

•	Изменение данных о количестве воды в зависимости от выбранной даты;

•	Изменение записи в личном дневнике;

•	Добавление данных о тренировка;

•	Добавление данных о питании;

•	Добавление данных о таблетках;

•	Добавление записей в личный дневник;

•	Добавление списка задач;

•	Добавление вида тренировок;

•	Просмотр списка советов;

•	Отметка пройденного врача и обследования;

•	Отмена отметки пройденного врача и обследования;

•	Отметка сделанной задачи;

•	Отмена отметки сделанной задачи;

•	Удаление данных о тренировках;

•	Удаление данных о питание;

•	Удаление данных о таблетках;

•	Удаление данных из списка задач;

•	Удаление записи в личном дневнике;

•	Изменение веса.

<div align="center">
  <img src="https://github.com/user-attachments/assets/79a71cb3-382f-4ca7-b758-8df015f80191" alt="Отзывы о программе" width="100%">
</div>


## Архитектура приложения
Компонент пользовательский интерфейс виден пользователю и взаимодействует с ним. В зависимости от действия пользователя осуществляются запросы к API, которая возвращает ответы на запрос в виде данных.  API используется для взаимодействия с базой данных Microsoft SQL Server, на котором хранятся данные, и для получения и отправки данных.

<div align="center">
  <img src="https://github.com/user-attachments/assets/4ed4a529-9532-42ae-af23-78a16809394d" alt="Отзывы о программе" width="80%">
</div>

## Примеры страниц мобильного приложения

<div align="center">
  <img src="https://github.com/user-attachments/assets/2926a370-927b-4ccb-85e4-6aaef6b1cf0b" alt="image1" width="30%">
  <img src="https://github.com/user-attachments/assets/0c347b8d-5cc5-4261-b9d6-a2a07c9f0ea2" alt="image2" width="30%">
  <img src="https://github.com/user-attachments/assets/658accd8-12b1-4478-8436-edd59f4c9c89" alt="image3" width="30%">
  <img src="https://github.com/user-attachments/assets/3aa23ff7-c2d2-496a-a00d-5979642ac229" alt="image4" width="30%">
  <img src="https://github.com/user-attachments/assets/656f4f15-c734-426e-b767-c372a4d08ab3" alt="image5" width="30%">
  <img src="https://github.com/user-attachments/assets/dd064f4e-7f53-4970-b8e6-fee5fdf6b7af" alt="image6" width="30%">
  <img src="https://github.com/user-attachments/assets/5a9c8fa1-c029-45db-8d3b-1b7a893a7818" alt="image7" width="30%">
  <img src="https://github.com/user-attachments/assets/2a480806-84a2-4c49-96b5-7222275a045d" alt="image8" width="30%">
  <img src="https://github.com/user-attachments/assets/67deb427-52df-436f-951e-8222b3532178" alt="image9" width="30%">
  <img src="https://github.com/user-attachments/assets/2a82f0f7-7d7f-4f80-be81-ac54e7f35e82" alt="image10" width="30%">
  <img src="https://github.com/user-attachments/assets/64ebc79e-759e-45e9-8b80-df4a5b52097c" alt="image11" width="30%">
  <img src="https://github.com/user-attachments/assets/23f7ca84-b239-497a-88e4-269206f717f2" alt="image12" width="30%">
  <img src="https://github.com/user-attachments/assets/6b804db2-da7d-463c-9d0f-7c9c925a3cd2" alt="image13" width="30%">
  <img src="https://github.com/user-attachments/assets/d14baef1-a755-4e19-84a8-53fc59e35493" alt="image14" width="30%">
  <img src="https://github.com/user-attachments/assets/8c042383-5506-4880-abcd-1c07b2c18604" alt="image15" width="30%">
  <img src="https://github.com/user-attachments/assets/d5945953-489e-4bde-8ac6-e9f6633a7c81" alt="image16" width="30%">
  <img src="https://github.com/user-attachments/assets/710cb5a0-b130-4fae-8a8b-fe347223aab9" alt="image17" width="30%">
  <img src="https://github.com/user-attachments/assets/536e535d-2d9a-4e66-9c19-bba35313b41d" alt="image18" width="30%">
  <img src="https://github.com/user-attachments/assets/8e0d5d4c-b07a-40dc-851c-351e69400cf4" alt="image19" width="30%">
  <img src="https://github.com/user-attachments/assets/be08df39-340a-4e24-83df-3e1e1db33417" alt="image20" width="30%">
  <img src="https://github.com/user-attachments/assets/505494a0-0d95-49d6-91e3-0a35c5cc33e0" alt="image21" width="30%">
  <img src="https://github.com/user-attachments/assets/d1903e91-0284-4638-8557-fe5abdd56b4a" alt="image22" width="30%">
  <img src="https://github.com/user-attachments/assets/935a4d86-add5-4ae8-907c-a911efade07c" alt="image23" width="30%">
  <img src="https://github.com/user-attachments/assets/579fcdc3-5936-4118-b651-d1f842913303" alt="image24" width="30%">
  <img src="https://github.com/user-attachments/assets/d664cd0e-332e-467b-94dc-18ec365e5b84" alt="image25" width="30%">
  <img src="https://github.com/user-attachments/assets/a08f216d-b852-4560-9148-93419c1067e6" alt="image26" width="30%">
  <img src="https://github.com/user-attachments/assets/4813ca6e-a310-4b1e-aae8-3a7e9b047d48" alt="image27" width="30%">
  <img src="https://github.com/user-attachments/assets/a44b9123-9340-465e-b321-b8a92c7de97a" alt="image28" width="30%">
  <img src="https://github.com/user-attachments/assets/feb9150c-15b1-4435-85c4-66f00e22ce2e" alt="image29" width="30%">
  <img src="https://github.com/user-attachments/assets/cf1a9f64-e7a2-40d0-818d-0ce0dcaf562b" alt="image30" width="30%">
  <img src="https://github.com/user-attachments/assets/da6b36dc-6dc2-4b5d-b205-6ca40935bc70" alt="image31" width="30%">
  <img src="https://github.com/user-attachments/assets/7ecccecc-fa66-477c-9947-d09289e521f7" alt="image32" width="30%">
</div>











