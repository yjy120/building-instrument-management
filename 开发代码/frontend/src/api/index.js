import axios from 'axios'

const request = axios.create({
  baseURL: '/api',
  timeout: 10000
})

request.interceptors.request.use(
  config => {
    const token = localStorage.getItem('token')
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  },
  error => {
    return Promise.reject(error)
  }
)

request.interceptors.response.use(
  response => {
    return response.data
  },
  error => {
    if (error.response && error.response.status === 401) {
      localStorage.removeItem('token')
      localStorage.removeItem('user')
      window.location.href = '/login'
    }
    return Promise.reject(error)
  }
)

export const authApi = {
  login(data) {
    return request.post('/auth/login', data)
  },
  register(data) {
    return request.post('/auth/register', data)
  }
}

export const deviceApi = {
  getAll() {
    return request.get('/device')
  },
  getById(id) {
    return request.get(`/device/${id}`)
  },
  search(params) {
    return request.get('/device/search', { params })
  },
  create(data) {
    return request.post('/device', data)
  },
  update(id, data) {
    return request.put(`/device/${id}`, data)
  },
  delete(id) {
    return request.delete(`/device/${id}`)
  },
  getActive() {
    return request.get('/device/active')
  },
  getCalibrationDue() {
    return request.get('/device/calibration-due')
  }
}

export const stockApi = {
  getAll() {
    return request.get('/stock')
  },
  getByDeviceId(deviceId) {
    return request.get(`/stock/device/${deviceId}`)
  },
  getRecords(deviceId) {
    return request.get(`/stock/records/${deviceId}`)
  },
  adjust(params) {
    return request.post('/stock/adjust', null, { params })
  },
  updateThreshold(params) {
    return request.put('/stock/threshold', null, { params })
  }
}

export const purchaseApi = {
  getAllApplies() {
    return request.get('/purchase/apply')
  },
  getPendingApplies() {
    return request.get('/purchase/apply/pending')
  },
  getAppliesByUser(userId) {
    return request.get(`/purchase/apply/user/${userId}`)
  },
  createApply(data, userId) {
    return request.post('/purchase/apply', data, { params: { userId } })
  },
  updateApplyStatus(id, params) {
    return request.put(`/purchase/apply/${id}`, null, { params })
  },
  getAllTotals() {
    return request.get('/purchase/total')
  },
  createTotal(data) {
    return request.post('/purchase/total', data)
  },
  reviewTotal(id, params) {
    return request.put(`/purchase/total/${id}`, null, { params })
  }
}

export const useRecordApi = {
  getAll() {
    return request.get('/use-record')
  },
  getByUser(userId) {
    return request.get(`/use-record/user/${userId}`)
  },
  getByDevice(deviceId) {
    return request.get(`/use-record/device/${deviceId}`)
  },
  getBorrowed() {
    return request.get('/use-record/borrowed')
  },
  create(data, userId) {
    return request.post('/use-record', data, { params: { userId } })
  },
  returnDevice(id) {
    return request.put(`/use-record/return/${id}`)
  }
}

export const calibrationApi = {
  getAll() {
    return request.get('/calibration')
  },
  getByDevice(deviceId) {
    return request.get(`/calibration/device/${deviceId}`)
  },
  create(data) {
    return request.post('/calibration', data)
  }
}

export const repairApi = {
  getAll() {
    return request.get('/repair')
  },
  getPending() {
    return request.get('/repair/pending')
  },
  getByUser(userId) {
    return request.get(`/repair/user/${userId}`)
  },
  create(data, userId) {
    return request.post('/repair', data, { params: { userId } })
  },
  updateStatus(id, params) {
    return request.put(`/repair/${id}`, null, { params })
  }
}

export const issueReportApi = {
  getAll() {
    return request.get('/issue-report')
  },
  getPending() {
    return request.get('/issue-report/pending')
  },
  getByUser(userId) {
    return request.get(`/issue-report/user/${userId}`)
  },
  create(data, userId) {
    return request.post('/issue-report', data, { params: { userId } })
  },
  handleRepair(id, handlerId) {
    return request.post(`/issue-report/${id}/handle-repair`, null, { params: { handlerId } })
  },
  handleScrap(id, handlerId) {
    return request.post(`/issue-report/${id}/handle-scrap`, null, { params: { handlerId } })
  }
}

export const scrapApi = {
  getAll() {
    return request.get('/scrap')
  },
  getPending() {
    return request.get('/scrap/pending')
  },
  getByUser(userId) {
    return request.get(`/scrap/user/${userId}`)
  },
  create(data, userId) {
    return request.post('/scrap', data, { params: { userId } })
  },
  review(id, params) {
    return request.put(`/scrap/${id}`, null, { params })
  }
}

export const reportApi = {
  getAll() {
    return request.get('/report/all')
  },
  getDevice() {
    return request.get('/report/device')
  },
  getRepair() {
    return request.get('/report/repair')
  },
  getScrap() {
    return request.get('/report/scrap')
  },
  getUse() {
    return request.get('/report/use')
  }
}

export const dashboardApi = {
  getOverview() {
    return request.get('/dashboard/overview')
  },
  getCalibrationDueList() {
    return request.get('/dashboard/calibration-due-list')
  },
  getLowStockList() {
    return request.get('/dashboard/low-stock-list')
  },
  getRecentRecords() {
    return request.get('/dashboard/recent-records')
  }
}
