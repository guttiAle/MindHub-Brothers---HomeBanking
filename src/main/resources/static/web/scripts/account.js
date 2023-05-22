const {createApp} = Vue
let valorID = (new URLSearchParams(location.search)).get("id")
// const url = `http://localhost:8080/api/accounts/${valorID}`
const url = `/api/clients/current`

createApp({
    data(){
        return{
            data: [],
            accounts: [],
            account: [],
            fromDate: '',
            toDate: ''
        }
    },
    created(){
        axios.get(url)
        .then(response =>{
            this.accounts = response.data.accounts
            this.filter(this.accounts)
            this.account.transactions.sort((a, b) => b.id - a.id)
            console.log(this.account.transactions.sort((a, b) => b.id - a.id))

        })
        .catch(err => console.log(err))
    },
    methods: {
        logout() {
            Swal.fire({
                title: 'Sure you want to log out?',
                icon: 'warning',
                showCancelButton: true,
                confirmButtonColor: '#3085d6',
                cancelButtonColor: '#d33',
                confirmButtonText: 'Yes'
            }).then((result) => {
                if (result.isConfirmed) {
                    axios
                    .post('/api/logout')
                    .then(response => {window.location.replace('./index.html')})
                    .catch(error => {
                        if (error.response.status === 403) {
                            Swal.fire({
                                icon: 'error',
                                text: error.response.data,
                            })
                        } else {
                            console.log(error)
                        }
                    })
                }
            })},
        filter(list) {
            for (let i = 0; i < list.length; i++) {
                if(list[i].id == valorID){

                    this.account = list[i]
                }
            }
        },
        getTransactionsHistory(){
            console.log('From:', this.fromDate)
            console.log('To:', this.toDate)
            console.log(this.account.number)
            if(this.fromDate.length == 0 || this.toDate.length == 0){
                axios.get('/api/transactions', `accountNumber=${this.account.number}&start=all&end=all`)
                axios({
                    method: 'GET',
                    url: '/api/transactions',
                    responseType: 'blob',
                    params: {
                        accountNumber: `${this.account.number}`,
                        start: 'all',
                        end: 'all'
                        }
                    })
                        .then(response => {
                        const url = window.URL.createObjectURL(new Blob([response.data]))
                    
                        const link = document.createElement('a')
                        link.href = url
                        link.setAttribute('download', 'transaction-history.pdf')
                        document.body.appendChild(link)
                    
                        link.click()
                    
                        window.URL.revokeObjectURL(url)
                        })
                        .catch(error => {
                        console.error(error)
                })
            } else {
                window.location.replace(`/api/transactions?accountNumber=${this.account.number}&start=${this.fromDate}&end=${this.toDate}`)
            }
        }
    }
}).mount("#app")
