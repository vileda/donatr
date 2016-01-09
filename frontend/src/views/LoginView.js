import React from 'react'
import { connect } from 'react-redux'
import { actions as sessionActions } from '../redux/modules/session'
import { pushPath } from 'redux-simple-router'
import TextField from 'material-ui/lib/text-field'
import RaisedButton from 'material-ui/lib/raised-button'
import Card from 'material-ui/lib/card/card'

const mapStateToProps = (state) => ({
  session: state.session
})
export class LoginView extends React.Component {
  static propTypes = {
    session: React.PropTypes.object.isRequired,
    location: React.PropTypes.object.isRequired,
    params: React.PropTypes.object.isRequired,
    dispatch: React.PropTypes.func.isRequired
  };

  constructor (props) {
    super(props)
    this.onSubmit = this.onSubmit.bind(this)
  }

  componentWillMount () {
    this.checkAuth()
  }

  componentWillReceiveProps (nextProps) {
    this.checkAuth(
      nextProps.session.isAuthenticated,
      nextProps.session.triedToAuthenticate
    )
  }

  checkAuth (isAuthenticated, triedToAuthenticate) {
    triedToAuthenticate = triedToAuthenticate || this.props.session.triedToAuthenticate
    isAuthenticated = isAuthenticated || this.props.session.isAuthenticated
    if (!triedToAuthenticate) this.props.dispatch(sessionActions.tryToAuthenticate())
    if (triedToAuthenticate && isAuthenticated) {
      let redirectAfterLogin = this.props.location.query.next || '/'
      this.props.dispatch(pushPath(redirectAfterLogin))
    }
  }

  onSubmit (e) {
    e.preventDefault()
    let username = e.target.elements['username'].value
    let password = e.target.elements['password'].value
    this.props.dispatch(sessionActions.create(username, password))
  }

  render () {
    const { triedToAuthenticate, isAuthenticated } = this.props.session
    return (triedToAuthenticate && !isAuthenticated)
      ? (
      <div className='flex-container'>
        <Card style={{ padding: 10, color: '#f1f1f1' }}>
          <div>
            <div style={{color: '#f1f1f1'}} className='flex-container'>
              <h1>donatr</h1>
            </div>
            <div className='flex-container'>
              <h3>login</h3>
            </div>
            { this.props.session.loginFailed
              ? <div className='flex-container'>
              <div className='alert danger'>Login failed!</div>
            </div>
              : ''
            }
            <div className='flex-container'>
              <form autoComplete='fooo' onSubmit={this.onSubmit} action='/login' method='post'>
                <div><TextField autoComplete='username' floatingLabelText='Username' name='username'/></div>
                <div><TextField autoComplete='password' floatingLabelText='Password' type='password' name='password'/></div>
                <div style={{marginTop: 20}} className='flex-container'>
                  <RaisedButton secondary type='submit'>Login</RaisedButton>
                </div>
              </form>
            </div>
          </div>
        </Card>
      </div>)
      : null
  }
}

export default connect(mapStateToProps)(LoginView)
