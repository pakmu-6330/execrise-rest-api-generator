function Generator(baseURL) {
    this.baseURL = baseURL;
    this.token = "";
    this.sessionUser = {};
    this._urlBuilder.setBaseURL(baseURL);
}

Generator.prototype = {
    constructor: Generator,
    _urlBuilder: {
        setBaseURL: function (baseURL) {
            this.baseURL = baseURL;
        },
        login: function () {
            return this.baseURL + "/token";
        },
        logout: function () {
            return this.baseURL + "/token";
        },
        verify: function (verifyCode) {
            return this.baseURL + "/users/verify/" + verifyCode;
        },
        renew: function () {
            return this.baseURL + "/token/renew"
        },
        register: function () {
            return this.baseURL + "/users";
        },
        createProject: function () {
            return this.baseURL + "/projects";
        },
        getProject: function (projectId) {
            return this.baseURL + "/projects/" + projectId;
        },
        createEntity: function (projectId) {
            return this.baseURL + "/projects/" + projectId + "/entities";
        },
        createAttribute: function (projectId, entityName) {
            return this.baseURL + "/projects/" + projectId + "/entities/" + entityName + "/attributes";
        }, 
        getAttributes: function (projectId, entityName) {
            return this.baseURL + "/projects/" + projectId + "/entities/" + entityName + "/attributes";
        },
        createDbInfo: function (projectId) {
            return this.baseURL + "/projects/" + projectId + "/db";
        }, 
        createJob: function (projectId) {
            return this.baseURL + "/jobs/" + projectId;
        }, 
        createRelationship: function(projectId) {
            return this.baseURL + "/projects/" + projectId + "/relationships"
        }
    },
    _buildHeaders: function () {
        return {
            "Authorization": this.token
        }
    },
    _updateToken: function (token) {
        this.token = token;
    },
    // --- 用户注册 ---
    register: function (username, password, email) {
        var selfRef = this;

        return new Promise(function (resolve, reject) {
            $.ajax(selfRef._urlBuilder.register(), {
                data: {
                    "username": username,
                    "password": password,
                    "email": email
                },
                type: "POST"
            }).done(function (data) {
                var meta = data["meta"];
                if (meta["code"] === 0) {
                    resolve(data["data"]);
                } else {
                    reject(data);
                }
            }).fail(function (data) {
                reject(data);
            });
        });
    },
    // --- 执行验证码对应的操作 ---
    verify: function (verify) {
        $.ajax(this._urlBuilder.verify(verify), {
            type: "POST"
        }).done(function (data) {
            console.log(data);
        }).fail(function (data) {
            console.log(data);
        });

        return this;
    },
    // --- 用户登录 ---
    login: function (username, password) {
        var selfRef = this;

        return new Promise(function (resolve, reject) {
            $.ajax(selfRef._urlBuilder.login(), {
                data: {
                    "username": username,
                    "password": password
                },
                type: "POST"
            }).done(
                function (responseData) {
                    var meta = responseData["meta"];
                    if (meta) {
                        selfRef.sessionUser = responseData["data"];
                        selfRef._updateToken(responseData["data"]["token"]);
                        resolve(responseData["data"]);
                    } else {
                        reject(responseData);
                    }
                }
            ).fail(function (responseData) {
                reject(responseData);
            });
        });
    },
    // --- 用户注销 ---
    logout: function () {
        if (this.token === "") {
            console.warn("not login, skipped");
            return this;
        }

        var selfRef = this;
        return new Promise(function (resolve, reject) {
            $.ajax(selfRef._urlBuilder.logout(), {
                headers: selfRef._buildHeaders(),
                type: "DELETE"
            }).done(function (data) {
                resolve(data);
            }).fail(function (data) {
                reject(data);
            });
        });
    },
    // --- Token 续租 ---
    renewToken: function () {
        if (this.token === "") {
            console.warn("token not found, renew token action wouldn't execute");
            return this;
        }

        var selfRef = this;

        return new Promise(function (resolve, reject) {
            $.ajax(selfRef._urlBuilder.renew(), {
                headers: selfRef._buildHeaders(),
                type: "POST"
            }).done(function (data) {
                resolve(data["data"]);
            }).fail(function (data) {
                reject(data);
            });
        });
    },
    // --- 工程相关的操作 ---
    // --- 创建工程 ---
    createProject: function (data) {
        var selfRef = this;

        return new Promise(function (resolve, reject) {
            $.ajax(selfRef._urlBuilder.createProject(), {
                headers: selfRef._buildHeaders(),
                type: "POST",
                data: data
            }).done(function (responseData) {
                resolve(responseData["data"]);
            }).fail(function (responseData) {
                reject(responseData);
            });
        });
    },
    // --- 获得工程信息 ---
    getProject: function (projectId) {
        var selfRef = this;

        return new Promise(function (resolve, reject) {
            $.ajax(selfRef._urlBuilder.getProject(projectId), {
                headers: selfRef._buildHeaders(),
                type: "GET"
            }).done(function (data) {
                resolve(data["data"]);
            }).fail(function (data) {
                reject(data);
            });
        });
    },
    // --- 保存工程的数据库信息 ---
    createDatabaseInfo: function(data) {
        var selfRef = this;
        var projectId = data["projectId"];

        delete data["projectId"];

        return new Promise(function (resolve, reject) {
            $.ajax(selfRef._urlBuilder.createDbInfo(projectId), {
                headers: selfRef._buildHeaders(),
                type: "POST", 
                data: data
            }).done(function(data) {
                resolve(data["data"]);
            }).fail(function(data) {
                reject(data);
            });
        });
    },
    // --- 创建一个实体信息 ---
    createEntity: function (data) {
        var selfRef = this;
        var projectId = data["projectId"];

        delete data["projectId"];

        return new Promise(function (resolve, reject) {
            $.ajax(selfRef._urlBuilder.createEntity(projectId), {
                headers: selfRef._buildHeaders(),
                type: "POST",
                data: data
            }).done(function (responseData) {
                resolve(responseData["data"]);
            }).fail(function (responseData) {
                reject(responseData);
            });
        });
    },
    // --- 在实体当中添加一个属性 ---
    createAttribute: function (data) {
        var selfRef = this;
        var projectId = data["projectId"];
        var entityName = data["entityName"];

        delete data["projectId"];
        delete data["entityName"];

        return new Promise(function (resolve, reject) {
            var url = selfRef._urlBuilder.createAttribute(projectId, entityName);
            $.ajax(url, {
                headers: selfRef._buildHeaders(),
                type: "POST",
                data: data
            }).done(function (data) {
                resolve(data["data"]);
            }).fail(function (data) {
                reject(data);
            });
        });
    },
    // --- 创建一个代码生成作业 ---
    createJob: function(projectId) {
        var selfRef = this;

        return new Promise(function(resolve, reject) {
            $.ajax(selfRef._urlBuilder.createJob(projectId), {
                headers: selfRef._buildHeaders(), 
                type: "POST"
            }).done(function(data) {
                resolve(data["data"]);
            }).fail(function(data) {
                reject(data);
            });
        });
    }, 
    // --- 创建一个关联关系 ---
    createRelationship: function(data) {
        var selfRef = this;
        var projectId = data["projectId"];

        delete data["projectId"];

        return new Promise(function(resolve, reject) {
            $.ajax(selfRef._urlBuilder.createRelationship(projectId), {
                headers: selfRef._buildHeaders(), 
                type: "POST", 
                data: data
            }).done(function (data) {
                resolve(data["data"]);
            }).fail(function (data) {
                reject(data);
            });
        });      
    }
};
