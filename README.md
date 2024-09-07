# DEPRECATED
## **API**
Бэк создан исключительно для личного пользования и тестов.
В нем могут(100%) присутствовать различного рода и вида баги.

Host: "https://meetingapp-tj94.onrender.com"

Content-Type: application/json

**ИСПОЛЬЗУЕТСЯ ВО ВСЕХ МЕТОДАХ, КРОМЕ /login** Authorization: Bearer 'token'

Особенности сервера:
Может не работать из-за бездействия. Это связано с бесплатным тестовым доступом.

Обработка ошибок:
Их очень мало. Самые базовые и на действительный токен.
#### **Регистрация/логин**
Регистрация и логин происходит по одному общему методу из-за невозможности отправлять СМС-код.
В первый раз происходит регистрация. Во второй раз происходит авторизация, где сравнивается code и происходит авторизация с новым токеном.

### **/login @POST**
Запрос:
```json
{
    "phoneNumber": "89377118927",
    "code": "1234"
}
```
Ответ:
```json
{
    "success": true,
    "data": {
        "userModel": {
            "id": "668eb792-e62b-435d-8b62-a34cb96b4aca",
            "hasForm": true,
            "phoneNumber": "89377118920",
            "firstName": "Dmitriy",
            "secondName": "Chugunov",
            "avatar": ""
        },
        "token": "c3f1d35f-0c44-4386-a98e-664b5497b09e"
    }
}
```
#### **Обновление информации о user**
Пока только для firstName и lastName. Так же при success ответе меняется поле hasForm на true
### **/update @POST**
Запрос:
```json
{
    "firstName": "Dmitriy",
    "secondName": "Chugunov"
}
```
Ответ:
```json
{
    "success": true,
    "data": {
        "id": "668eb792-e62b-435d-8b62-a34cb96b4aca",
        "hasForm": true,
        "phoneNumber": "89377118920",
        "firstName": "Dmitriy",
        "secondName": "Chugunov",
        "avatar": ""
    }
}
```

#### **События**
### **/fetchAllMeetings @GET**
Получаем все события

Ответ:
```json
{
    "success": true,
    "data": [
        {
            "id": "1",
            "name": "Kotlin",
            "date": "28.08.2024",
            "location": "Россия, город Волгоград, улица Пушкина, дом Кукушкина 123",
            "tagList": [
                "kotlin",
                "android"
            ],
            "meetingUrl": "https://i.imgur.com/mbZtfw5.png",
            "isFinished": true,
            "isAttending": false,
            "description": "Обсуждение новых возможностей PHP и лучших практик для создания веб-приложений. Включает обзор нововведений в последних версиях и примеры кода.",
            "participants": []
        },
        ...
    ]
}
```

### **/fetchMyMeetings @GET**
Получаем все события текущего(авторизованного) юзера

Ответ:
```json
{
    "success": true,
    "data": [
        {
            "id": "1",
            "name": "Kotlin",
            "date": "28.08.2024",
            "location": "Россия, город Волгоград, улица Пушкина, дом Кукушкина 123",
            "tagList": [
                "kotlin",
                "android"
            ],
            "meetingUrl": "https://i.imgur.com/mbZtfw5.png",
            "isFinished": true,
            "isAttending": false,
            "description": "Обсуждение новых возможностей PHP и лучших практик для создания веб-приложений. Включает обзор нововведений в последних версиях и примеры кода.",
            "participants": [
                {
                    "id": "1fe76210-86b9-4362-a332-65aca990665d",
                    "hasForm": false,
                    "phoneNumber": "89377118920",
                    "firstName": "Dmitriy",
                    "secondName": "Chugunov",
                    "avatar": ""
                }
            ]
        },
        ...
    ]
}
```

### **/fetchActiveMeetings @GET**
Получаем все активные события(isFinished == false)

Ответ:
```json
{
    "success": true,
    "data": [
        {
            "id": "1",
            "name": "Kotlin",
            "date": "28.08.2024",
            "location": "Россия, город Волгоград, улица Пушкина, дом Кукушкина 123",
            "tagList": [
                "kotlin",
                "android"
            ],
            "meetingUrl": "https://i.imgur.com/mbZtfw5.png",
            "isFinished": false,
            "isAttending": false,
            "description": "Обсуждение новых возможностей PHP и лучших практик для создания веб-приложений. Включает обзор нововведений в последних версиях и примеры кода.",
            "participants": []
        },
        ...
    ]
}
```

### **/fetchActiveMeetings @GET**
Получаем все активные события(isFinished == false)

Ответ:
```json
{
    "success": true,
    "data": [
        {
            "id": "1",
            "name": "Kotlin",
            "date": "28.08.2024",
            "location": "Россия, город Волгоград, улица Пушкина, дом Кукушкина 123",
            "tagList": [
                "kotlin",
                "android"
            ],
            "meetingUrl": "https://i.imgur.com/mbZtfw5.png",
            "isFinished": false,
            "isAttending": false,
            "description": "Обсуждение новых возможностей PHP и лучших практик для создания веб-приложений. Включает обзор нововведений в последних версиях и примеры кода.",
            "participants": []
        },
        ...
    ]
}
```

### **/fetchFinishedMeetings @GET**
Получаем все завершенные события юзера(isFinished == true)

Ответ:
```json
{
    "success": true,
    "data": [
        {
            "id": "1",
            "name": "Kotlin",
            "date": "28.08.2024",
            "location": "Россия, город Волгоград, улица Пушкина, дом Кукушкина 123",
            "tagList": [
                "kotlin",
                "android"
            ],
            "meetingUrl": "https://i.imgur.com/mbZtfw5.png",
            "isFinished": true,
            "isAttending": true,
            "description": "Обсуждение новых возможностей PHP и лучших практик для создания веб-приложений. Включает обзор нововведений в последних версиях и примеры кода.",
            "participants": [
                {
                    "id": "1fe76210-86b9-4362-a332-65aca990665d",
                    "hasForm": false,
                    "phoneNumber": "89377118920",
                    "firstName": "Dmitriy",
                    "secondName": "Chugunov",
                    "avatar": ""
                }
            ]
        },
        ...
    ]
}
```

### **/fetchMeetingById?meetingId=1 @GET**
Получаем событие по ID.
Обязательный параметр meetingId: String

Ответ:
```json
{
    "success": true,
    "data": {
        "id": "1",
        "name": "Kotlin devs",
        "date": "05.08.2024",
        "location": "Россия, город Волгоград, улица Пушкина, дом Кукушкина 123",
        "tagList": [
            "kotlin",
            "android"
        ],
        "meetingUrl": null,
        "isFinished": false,
        "isAttending": true,
        "description": null,
        "participants": [
            {
                "id": "1fe76210-86b9-4362-a332-65aca990665d",
                "hasForm": true,
                "phoneNumber": "89377118920",
                "firstName": "Dmitriy",
                "secondName": "Chugunov",
                "avatar": ""
            }
        ]
    }
}
```

### **/toggleAttendance?meetingId=2 @POST**
Меняем значение присутствия(удаляемся/добавляемся из/в событии)
Обязательный параметр meetingId: String

Ответ:
```json
{
    "success": true,
    "data": {
        "id": "1",
        "name": "Kotlin devs",
        "date": "05.08.2024",
        "location": "Россия, город Волгоград, улица Пушкина, дом Кукушкина 123",
        "tagList": [
            "kotlin",
            "android"
        ],
        "meetingUrl": null,
        "isFinished": false,
        "isAttending": true,
        "description": null,
        "participants": [
            {
                "id": "1fe76210-86b9-4362-a332-65aca990665d",
                "hasForm": true,
                "phoneNumber": "89377118920",
                "firstName": "Dmitriy",
                "secondName": "Chugunov",
                "avatar": ""
            }
        ]
    }
}
```

### **/fetchAllCommunities @GET**
Получаем список всех сообществ

Ответ:
```json
{
    "success": true,
    "data": [
        {
            "id": "2",
            "name": "Сбербанк",
            "size": "12",
            "avatar": "https://i.imgur.com/OYvwmck.png",
            "description": "«Сберба́нк» — российский финансовый конгломерат, крупнейший универсальный банк России и Восточной Европы. По итогам 2023 года у Сбербанка 108,5 млн активных частных клиентов и 3,2 млн активных корпоративных клиентов.",
            "meetings": []
        },
        {
            "id": "1",
            "name": "T-Bank",
            "size": "100",
            "avatar": "https://i.imgur.com/UIERt0L.png",
            "description": "«Т-Банк» — российский коммерческий банк, сфокусированный полностью на дистанционном обслуживании, не имеющий розничных отделений. Крупнейший в мире онлайн-банк по количеству клиентов. Занимает 9-ое место по размеру активов среди банков в России. Штаб-квартира банка расположена в Москве.",
            "meetings": [
                {
                    "id": "1",
                    "name": "Kotlin",
                    "date": "28.08.2024",
                    "location": "Россия, город Волгоград, улица Пушкина, дом Кукушкина 123",
                    "tagList": [
                        "kotlin",
                        "android"
                    ],
                    "meetingUrl": "https://i.imgur.com/mbZtfw5.png",
                    "isFinished": true,
                    "isAttending": false,
                    "description": "Обсуждение новых возможностей PHP и лучших практик для создания веб-приложений. Включает обзор нововведений в последних версиях и примеры кода.",
                    "participants": []
                }
            ]
        }
    ]
}
```
### **/fetchCommunityById?communityId=1 @GET**
Получаем сообщество по id
Обязательный параметр communityId: String

Ответ:
```json
{
    "success": true,
    "data": {
        "id": "1",
        "name": "T-Bank",
        "size": "100",
        "avatar": "https://i.imgur.com/UIERt0L.png",
        "description": "«Т-Банк» — российский коммерческий банк, сфокусированный полностью на дистанционном обслуживании, не имеющий розничных отделений. Крупнейший в мире онлайн-банк по количеству клиентов. Занимает 9-ое место по размеру активов среди банков в России. Штаб-квартира банка расположена в Москве.",
        "meetings": [
            {
                "id": "1",
                "name": "Kotlin",
                "date": "28.08.2024",
                "location": "Россия, город Волгоград, улица Пушкина, дом Кукушкина 123",
                "tagList": [
                    "kotlin",
                    "android"
                ],
                "meetingUrl": "https://i.imgur.com/mbZtfw5.png",
                "isFinished": true,
                "isAttending": false,
                "description": "Обсуждение новых возможностей PHP и лучших практик для создания веб-приложений. Включает обзор нововведений в последних версиях и примеры кода.",
                "participants": []
            }
        ]
    }
}
```

